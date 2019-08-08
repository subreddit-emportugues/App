package org.emportugues.aplicativo.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    private FloatingActionButton floatingActionButton;

    private ArrayList<Subreddit> subredditList;
    private MyListAdapter adapter;

    private String previousColumn = "";
    private boolean reversed = false;

    private Response<SubredditList> subredditListResponse;


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

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setAlpha(0.5f);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setSelection(0);
            }
        });
        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView.smoothScrollToPosition(0);
                return true;
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
                        subredditListResponse = response;
                        subredditList = response.body().getSubreddits("default", false);

                        /*
                         * Binding that List to Adapter
                         */
                        adapter = new MyListAdapter(MainActivity.this, response.body(), "default", false);
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

    public void sort(View v) {
        String column = v.getTag().toString();

        ImageView arrowSubredditAsc = findViewById(R.id.arrowSubredditAsc);
        ImageView arrowSubredditDesc = findViewById(R.id.arrowSubredditDesc);
        ImageView arrowDescriptionAsc = findViewById(R.id.arrowDescriptionAsc);
        ImageView arrowDescriptionDesc = findViewById(R.id.arrowDescriptionDesc);
        ImageView arrowActivityAsc = findViewById(R.id.arrowActivityAsc);
        ImageView arrowActivityDesc = findViewById(R.id.arrowActivityDesc);
        ImageView arrowMembersAsc = findViewById(R.id.arrowMembersAsc);
        ImageView arrowMembersDesc = findViewById(R.id.arrowMembersDesc);
        ImageView arrowAgeAsc = findViewById(R.id.arrowAgeAsc);
        ImageView arrowAgeDesc = findViewById(R.id.arrowAgeDesc);
        ImageView arrowModeratorsAsc = findViewById(R.id.arrowModeratorsAsc);
        ImageView arrowModeratorsDesc = findViewById(R.id.arrowModeratorsDesc);
        ImageView arrowNSFWAsc = findViewById(R.id.arrowNFSWAsc);
        ImageView arrowNSFWDesc = findViewById(R.id.arrowNSFWDesc);

        if (column.equals(previousColumn)) {
            reversed = !reversed;
        } else {
            reversed = false;
        }

        switch (column) {
            case "Subreddit":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "Descrição":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "Atividade":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "Membros":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "Criação":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "Moderadores":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;
            case "NSFW":
                if (column.equals(previousColumn) && reversed) {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                } else {
                    arrowSubredditAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowSubredditDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowDescriptionDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowActivityDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowMembersDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowAgeDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsAsc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowModeratorsDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                    arrowNSFWAsc.setColorFilter(getResources().getColor(R.color.colorOrangeA400));
                    arrowNSFWDesc.setColorFilter(getResources().getColor(R.color.colorBlueGray700));
                }
                break;

        }

        assert subredditListResponse.body() != null;

        subredditList = subredditListResponse.body().getSubreddits(column, reversed);
        adapter = new MyListAdapter(MainActivity.this, subredditListResponse.body(), column, reversed);
        previousColumn = column;
        listView.setAdapter(adapter);
    }

}
