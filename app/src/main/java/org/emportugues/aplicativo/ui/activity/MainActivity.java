package org.emportugues.aplicativo.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.adapter.MyListAdapter;
import org.emportugues.aplicativo.model.Subreddit;
import org.emportugues.aplicativo.model.SubredditList;
import org.emportugues.aplicativo.retrofit.api.ApiService;
import org.emportugues.aplicativo.retrofit.api.RetroClient;
import org.emportugues.aplicativo.utils.InternetConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * Views
     */
    private ListView listView;
    private View parentView;

    private ArrayList<Subreddit> subredditList;
    private MyListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Array List for Binding Data from JSON to this List
         */
        subredditList = new ArrayList<>();

        parentView = findViewById(R.id.parentLayout);

        /*
         * Getting List and Setting List Adapter
         */
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://reddit.com/" +
                        subredditList.get(position).getName()));
                startActivity(myIntent);
            }
        });

        /*
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /*
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getString(R.string.string_getting_json_title));
            dialog.setMessage(getString(R.string.string_getting_json_message));
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /*
             * Calling JSON
             */
            Call<SubredditList> call = api.getMyJSON();

            /*
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<SubredditList>() {
                @Override
                public void onResponse(Call<SubredditList> call, Response<SubredditList> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        /*
                         * Got Successfully
                         */
                        assert response.body() != null;
                        subredditList = response.body().getSubreddits();

                        /*
                         * Binding that List to Adapter
                         */
                        adapter = new MyListAdapter(MainActivity.this, response.body());
                        listView.setAdapter(adapter);

                    } else {
                        Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SubredditList> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Snackbar.make(parentView, R.string.string_internet_connection_not_available, Snackbar.LENGTH_LONG).show();
        }

    }

}
