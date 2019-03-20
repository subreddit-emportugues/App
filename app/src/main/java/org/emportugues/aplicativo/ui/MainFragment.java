package org.emportugues.aplicativo.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.evrencoskun.tableview.TableView;
import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.model.ServiceRequest;
import org.emportugues.aplicativo.ui.tableview.MyTableAdapter;
import org.emportugues.aplicativo.ui.tableview.MyTableViewListener;
import org.emportugues.aplicativo.ui.viewmodel.MainViewModel;
import org.emportugues.aplicativo.ui.viewmodel.MainViewModelFactory;
import org.emportugues.aplicativo.utility.InjectorUtils;

public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private TableView mTableView;
    private MyTableAdapter mTableAdapter;
    private ProgressBar mProgressBar;
    private MainViewModel vMainViewModel;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);

        mTableView = view.findViewById(R.id.my_TableView);

        initializeTableView(mTableView);

        // initialize ViewModel
        MainViewModelFactory factory = InjectorUtils.getMainViewModelFactory(getActivity().getApplicationContext());
        vMainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        vMainViewModel.getUserList().observe(this, users -> {

            if (users != null && users.size() > 0) {
                // set the list on TableViewModel
                mTableAdapter.setUserList(users);

                hideProgressBar();
            }
        });

        // Let's post a request to get the User data from a web server.
        postRequest();

        return view;
    }

    private void initializeTableView(TableView tableView) {

        // Create TableView Adapter
        mTableAdapter = new MyTableAdapter(getContext());
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new MyTableViewListener(tableView));
    }

    private void postRequest() {
        int size = 503; // this is the count of the data items.
        int page = 1; // Which page do we want to get from the server.
        ServiceRequest serviceRequest = new ServiceRequest(size, page);
        vMainViewModel.postRequest(serviceRequest);

        showProgressBar();
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.INVISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTableView.setVisibility(View.VISIBLE);
    }

}
