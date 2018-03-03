package com.mycoursesapp.helper;

import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUsersShowAPICustomService {
    @GET("/1.1/users/show.json")
    Call<User> show(@Query("user_id") long userId);

    /*
    * In retrofit v1 you could write like this
    *
    * @GET("/1.1/users/show.json")
    * void show(@Query("user_id") Long userId, Callback<User> cb);
    *
    * */
}
