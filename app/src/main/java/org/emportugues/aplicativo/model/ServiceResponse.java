package org.emportugues.aplicativo.model;

import org.emportugues.aplicativo.data.network.pojo.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {

    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;
    @SerializedName("current_page")
    @Expose
    public Integer currentPage;
    @SerializedName("last_page")
    @Expose
    public Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    public String nextPageUrl;
    @SerializedName("prev_page_url")
    @Expose
    public Object prevPageUrl;
    @SerializedName("from")
    @Expose
    public Integer from;
    @SerializedName("to")
    @Expose
    public Integer to;
    @SerializedName("subreddits")
    @Expose
    public List<Data> subreddits = null;

}
