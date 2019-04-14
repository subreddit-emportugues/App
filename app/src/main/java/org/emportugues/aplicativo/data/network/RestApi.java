package org.emportugues.aplicativo.data.network;

import org.emportugues.aplicativo.model.ServiceResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("subreddits.json")
    Observable<ServiceResponse> getUser(@Query("per_page") int size, @Query("current_page") int page);

}
