package org.emportugues.aplicativo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class SubredditList {

    @SerializedName("subreddits")
    @Expose
    private ArrayList<Subreddit> subreddits = new ArrayList<>();

    public ArrayList<Subreddit> getSubreddits(String sortingColumn, boolean reverse) {

        switch (sortingColumn) {
            case "Subreddit":
                Collections.sort(subreddits, new NameComparator());
                break;
            case "Descrição":
                Collections.sort(subreddits, new DescriptionComparator());
                break;
            case "Membros":
                Collections.sort(subreddits, new MemberComparator());
                break;
            case "Criação":
                Collections.sort(subreddits, new AgeComparator());
                break;
            case "Moderadores":
                Collections.sort(subreddits, new ModeratorComparator());
                break;
            case "NSFW":
                Collections.sort(subreddits, new NSFWComparator());
                break;
            case "Atividade":
                Collections.sort(subreddits, new ActivityComparator());
                break;
        }

        if (reverse) {
            Collections.reverse(subreddits);
        }

        return subreddits;
    }

    public void setSubreddits(ArrayList<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    int getTotalActivity() {
        int total = 0;

        for (Subreddit subreddit : subreddits) {
            total += subreddit.getComments() + subreddit.getSubmissions();
        }

        return total;
    }

}
