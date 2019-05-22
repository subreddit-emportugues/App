package org.emportugues.aplicativo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class SubredditList {

    @SerializedName("subreddits")
    @Expose
    private ArrayList<Subreddit> subreddits = new ArrayList<>();

    public ArrayList<Subreddit> getSubreddits() {
        Collections.sort(subreddits, new ActivityDescComparator());
        return subreddits;
    }

    public void setSubreddits(ArrayList<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    public int getActivity() {
        int total = 0;

        for (Subreddit subreddit : subreddits) {
            total += subreddit.getComments() + subreddit.getSubmissions();
        }

        return total;
    }

}
