package org.emportugues.aplicativo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.adapter.GlideApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        Intent intent = getIntent();
        String subredditIcon = intent.getStringExtra("Icon");
        String subredditName = intent.getStringExtra("Name");
        String subredditDescription = intent.getStringExtra("Description");
        String subredditSubmissions = intent.getStringExtra("Submissions");
        String subredditComments = intent.getStringExtra("Comments");
        String subredditMembers = intent.getStringExtra("Members");
        long subredditAge = intent.getLongExtra("Age", 0);
        ArrayList<String> subredditModerators = intent.getStringArrayListExtra("Moderators");
        boolean subredditNSFW = intent.getBooleanExtra("NSFW", false);

        ButterKnife.bind(this);

        ImageView imageViewIcon = findViewById(R.id.imageViewIcon);
        final TextView textViewName = findViewById(R.id.textViewName);
        ExpandableTextView expandableTextView = findViewById(R.id.expandableTextView);
        ImageView imageViewNSFW = findViewById(R.id.imageViewNSFW);

        assert subredditIcon != null;
        if (subredditIcon.startsWith("https://")) { //url.isEmpty()
            GlideApp.with(getApplicationContext())
                    .load(subredditIcon)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imageViewIcon);
        } else {
            GlideApp.with(getApplicationContext())
                    .load(getString(R.string.url_subreddit_icon))
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imageViewIcon);
        }

        textViewName.setText(subredditName);

        assert subredditDescription != null;
        String formatDescription = subredditDescription.replaceAll(System.getProperty("line.separator"), "");

        String calculatedSubmissions;
        assert subredditSubmissions != null;
        if (subredditSubmissions.equals("0")) {
            calculatedSubmissions = getString(R.string.zero_feminine) + getString(R.string.dialog_post);
        } else if (subredditSubmissions.equals("1")) {
            calculatedSubmissions = subredditSubmissions + getString(R.string.dialog_post);
        } else {
            calculatedSubmissions = subredditSubmissions + getString(R.string.dialog_posts);
        }
        String calculatedComments;
        assert subredditComments != null;
        if (subredditComments.equals("0")) {
            calculatedComments = getString(R.string.zero_masculine) + getString(R.string.dialog_comment);
        } else if (subredditComments.equals("1")) {
            calculatedComments = subredditComments + getString(R.string.dialog_comment);
        } else {
            calculatedComments = subredditComments + getString(R.string.dialog_comments);
        }
        String concatenateActivity = calculatedSubmissions + calculatedComments + getString(R.string.dialog_lastweek);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String stringDate = dateFormat.format(Calendar.getInstance().getTime());

        long timeDifference = System.currentTimeMillis() - subredditAge;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long mothsInMilli = daysInMilli * 30;
        long yearInMilli = mothsInMilli * 12;

        long elapsedYear = timeDifference / yearInMilli;
        timeDifference = timeDifference % yearInMilli;

        long elapsedMonths = timeDifference / mothsInMilli;
        timeDifference = timeDifference % mothsInMilli;

        long elapsedDays = timeDifference / daysInMilli;
        timeDifference = timeDifference % daysInMilli;

        String calculatedTime;
        if (elapsedYear > 0) {
            if (elapsedYear > 1) {
                calculatedTime = String.valueOf(elapsedYear).concat(getString(R.string.time_years));
            } else {
                calculatedTime = String.valueOf(elapsedYear).concat(getString(R.string.time_year));
            }
        } else if (elapsedMonths > 0) {
            if (elapsedMonths > 1) {
                calculatedTime = String.valueOf(elapsedMonths).concat(getString(R.string.time_months));
            } else {
                calculatedTime = String.valueOf(elapsedMonths).concat(getString(R.string.time_month));
            }
        } else {
            if (elapsedDays < 0) {
                calculatedTime = getString(R.string.time_today);
            } else if (elapsedDays > 1) {
                calculatedTime = String.valueOf(elapsedDays).concat(getString(R.string.time_days));
            } else {
                calculatedTime = String.valueOf(elapsedDays).concat(getString(R.string.time_day));
            }
        }

        String concatenateMembers = subredditMembers + getString(R.string.dialog_members) + stringDate + " " + getString(R.string.dialog_age) + calculatedTime;

        String calculatedModerators;
        assert subredditModerators != null;
        if (subredditModerators.toString().replace("[", "").replace("]", "").equals("")) {
            calculatedModerators = getString(R.string.dialog_moderator);
        } else {
            calculatedModerators = subredditModerators.toString().replace("[", "").replace("]", "");
        }
        String concatenateModerators = getString(R.string.dialog_moderators) + calculatedModerators + ".";

        expandableTextView.setText(
                formatDescription + getString(R.string.dialog_newline) +
                        concatenateActivity + getString(R.string.dialog_newline) +
                        concatenateMembers + getString(R.string.dialog_newline) +
                        concatenateModerators
        );

        if (subredditNSFW) {
            imageViewNSFW.setImageResource(R.drawable.ic_whatshot_black_24dp);
        }
    }

    @OnClick(R.id.linkButton)
    void submitButton(View view) {
        Intent intent = getIntent();
        String subredditName = intent.getStringExtra("Name");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://reddit.com/" +
                subredditName));
        startActivity(myIntent);
    }

}
