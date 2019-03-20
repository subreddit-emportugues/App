package org.emportugues.aplicativo.data.network;

import android.util.Log;

import org.emportugues.aplicativo.data.database.entity.User;
import org.emportugues.aplicativo.data.network.pojo.Data;
import org.emportugues.aplicativo.model.ServiceResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://pastebin.com/raw/";

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory
                .create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    public static Disposable getDataFromService(int size, int page,
                                                DisposableObserver<ServiceResponse> observer) {
        Log.d(LOG_TAG, "Getting data from the server");
        try {
            RestApi service = getRetrofit().create(RestApi.class);

            Observable<ServiceResponse> observable = service.getUser(size, page);
            return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread
                    ()).subscribeWith(observer);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Getting data process has been failed. ", e);
        }
        return null;
    }


    public static List<User> convertToUserList(ServiceResponse serviceResponse) {
        List<User> users = new ArrayList<>();

        Log.d(LOG_TAG, "Converting the response.");
        try {
            for (Data data : serviceResponse.data) {
                User user = new User();
                user.id = Integer.parseInt(data.id);
                user.subreddit = data.subreddit;
                user.description = data.description;
                user.nsfw = data.nsfw;
                user.age = getDate(new java.util.Date(data.age * 1000).toString());
                user.subscribers = data.subscribers;
                user.icon = data.icon;

                // add
                users.add(user);
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "Converting the response process has been failed. ", e);
        }

        return users;
    }

    private static String getDate(String stringData) throws ParseException {
        SimpleDateFormat inputDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.getDefault());
        Date date = inputDate.parse(stringData);
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        System.out.println(date);
        return outputDate.format(date);
    }

}
