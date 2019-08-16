package org.emportugues.aplicativo.ui.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.adapter.MyListAdapter;
import org.emportugues.aplicativo.model.Subreddit;
import org.emportugues.aplicativo.model.SubredditList;
import org.emportugues.aplicativo.retrofit.api.ApiService;
import org.emportugues.aplicativo.retrofit.api.RetroClient;
import org.emportugues.aplicativo.utils.InternetConnection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private View parentView;

    private ArrayList<Subreddit> subredditList;
    private MyListAdapter adapter;

    private String previousColumn = "";
    private boolean reversed = false;

    private Response<SubredditList> subredditListResponse;

    ActionBar.Tab textViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());

        // Array list for binding data from JSON to this list
        subredditList = new ArrayList<>();

        parentView = findViewById(R.id.parentLayout);

        // Getting list and setting list adapter
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://reddit.com/" +
                        subredditList.get(position).getName()));
                startActivity(myIntent);
            } else {
                Subreddit subredditPosition = adapter.getItem(position);
                Intent launchNewIntent = new Intent(MainActivity.this, DialogActivity.class);
                assert subredditPosition != null;
                launchNewIntent.putExtra("Icon", subredditPosition.getIcon());
                launchNewIntent.putExtra("Name", subredditPosition.getName());
                launchNewIntent.putExtra("Description", subredditPosition.getDescription());
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String getSubmissionsFormatted = formatter.format(subredditPosition.getSubmissions()).replace(",", ".");
                launchNewIntent.putExtra("Submissions", getSubmissionsFormatted);
                String getCommentsFormatted = formatter.format(subredditPosition.getComments()).replace(",", ".");
                launchNewIntent.putExtra("Comments", getCommentsFormatted);
                String getMembersFormatted = formatter.format(subredditPosition.getMembers()).replace(",", ".");
                launchNewIntent.putExtra("Members", getMembersFormatted);
                launchNewIntent.putExtra("Age", subredditPosition.getAge() * 1000);
                launchNewIntent.putStringArrayListExtra("Moderators", subredditPosition.getModerators());
                launchNewIntent.putExtra("NSFW", subredditPosition.getNSFW());
                startActivity(launchNewIntent);
            }
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://reddit.com/" +
                    subredditList.get(position).getName()));
            startActivity(myIntent);
            return true;
        });

        // Setting floating action button functionalities
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setAlpha(0.5f);
        floatingActionButton.setOnClickListener(view -> listView.setSelection(0));
        floatingActionButton.setOnLongClickListener(view -> {
            listView.smoothScrollToPosition(0);
            return true;
        });

        // Checking internet connection
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;

            // Progress dialog for user interaction
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getString(R.string.progress_dialog_title));
            dialog.setMessage(getString(R.string.progress_dialog_message));
            dialog.show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBlueGray900)));

            //Creating an object of our API interface
            ApiService api = RetroClient.getApiService();

            // Calling JSON
            Call<SubredditList> call = api.getMyJSON();

            // Enqueue callback will be call when get response...
            call.enqueue(new Callback<SubredditList>() {
                @Override
                public void onResponse(Call<SubredditList> call, Response<SubredditList> response) {
                    //Dismiss dialog
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Got successfully
                        assert response.body() != null;
                        subredditListResponse = response;
                        subredditList = response.body().getSubreddits("default", false);

                        // Binding that List to Adapter
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        hideKeyboard(MainActivity.this);
                        searchView.clearFocus();
                        return true;
                    }
                }
        );
        searchView.getQuery();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_github) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_github)));
            startActivity(myIntent);
        }
        if (id == R.id.menu_reddit) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_reddit)));
            startActivity(myIntent);
        }
        if (id == R.id.menu_site) {
            Intent launchNewIntent = new Intent(MainActivity.this, WebViewActivity.class);
            startActivityForResult(launchNewIntent, 0);
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        textViewSearch.setText(query);
    }

    public static void hideKeyboard(MainActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
