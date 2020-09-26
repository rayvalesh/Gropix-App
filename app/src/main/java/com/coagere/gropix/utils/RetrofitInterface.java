package com.coagere.gropix.utils;


import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RetrofitInterface {
    @PUT
    Call<Void> uploadBinaryFile(@Url String url, @Body RequestBody photo);

    @GET
    Call<Object> commonGet(@Url String url);

    @PUT
    Call<Object> commonPut(@Url String url, @Body HashMap<String, String> map);

    @POST
    Call<Object> commonPost(@Url String url, @Body HashMap<String, String> map);

}
