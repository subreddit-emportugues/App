package org.emportugues.aplicativo.retrofit.api;

import org.emportugues.aplicativo.model.SubredditList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of SubredditList
    */
    @GET("/data/subreddits.json")
    Call<SubredditList> getMyJSON();

}
