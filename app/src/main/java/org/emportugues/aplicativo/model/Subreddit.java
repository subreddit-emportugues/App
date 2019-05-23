package org.emportugues.aplicativo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;

public class Subreddit {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("recent_submissions")
    @Expose
    private int submissions;
    @SerializedName("recent_comments")
    @Expose
    private int comments;
    @SerializedName("total_activity")
    @Expose
    private double total_activity;
    @SerializedName("members")
    @Expose
    private Number members;
    @SerializedName("age")
    @Expose
    private Long age;
    @SerializedName("moderators")
    @Expose
    private String[] moderators;
    @SerializedName("nsfw")
    @Expose
    private Boolean nsfw;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSubmissions() {
        return submissions;
    }

    public void setSubmissions(int recent_submissions) {
        this.submissions = recent_submissions;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int recent_comments) {
        this.comments = recent_comments;
    }

    public String getTotalActivity(SubredditList subredditList) {
        DecimalFormatSymbols sep = new DecimalFormatSymbols();
        sep.setDecimalSeparator(',');

        double total = subredditList.getTotalActivity();
        total_activity = ((submissions + comments) / total) * 100;
        DecimalFormat df = new DecimalFormat("####0.00", sep);

        return (total_activity > 0 && total_activity < 0.01 ? "< 0,01" : df.format(total_activity)) + '%';
    }

    public Number getMembers() {
        return members;
    }

    public void setMembers(Number members) {
        this.members = members;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String[] getModerators() {
        return moderators;
    }

    public void setModerators(String[] moderators) {
        this.moderators = moderators;
    }

    public Boolean getNSFW() {
        return nsfw;
    }

    public void setNSFW(Boolean nsfw) {
        this.nsfw = nsfw;
    }

}

class NameComparator implements Comparator<Subreddit> {
    public int compare(Subreddit c1, Subreddit c2) {
        String s1 = c1.getName();
        String s2 = c2.getName();
        return s1.compareToIgnoreCase(s2);
    }

}

class DescriptionComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        String s1 = c1.getDescription();
        String s2 = c2.getDescription();
        return s1.compareToIgnoreCase(s2);
    }

}

class ActivityComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        return (c2.getSubmissions() + c2.getComments()) -
                (c1.getSubmissions() + c1.getComments());
    }

}

class MemberComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        return c2.getMembers().intValue() - c1.getMembers().intValue();
    }

}

class AgeComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        return c2.getAge().intValue() - c1.getAge().intValue();
    }

}

class ModeratorComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        return c2.getModerators().length - c1.getModerators().length;
    }

}

class NSFWComparator implements Comparator<Subreddit> {
    @Override
    public int compare(Subreddit c1, Subreddit c2) {
        return c2.getNSFW().compareTo(c1.getNSFW());
    }

}
