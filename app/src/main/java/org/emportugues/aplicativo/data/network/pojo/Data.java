package org.emportugues.aplicativo.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("nsfw")
    @Expose
    public Boolean nsfw;
    @SerializedName("age")
    @Expose
    public Long age;
    @SerializedName("members")
    @Expose
    public Integer members;
    @SerializedName("icon")
    @Expose
    public String icon;

}
