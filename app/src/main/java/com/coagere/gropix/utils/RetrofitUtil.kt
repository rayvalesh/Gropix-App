package com.coagere.gropix.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tc.utils.variables.interfaces.ApiKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Retrofit Util for Retrofit Initialization and Instance signup
 *
 * @author Jatin Sahgal
 */
class RetrofitUtil {
    private var retrofit: RetrofitInterface? = null
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun callRetrofit(): RetrofitInterface {
        if (retrofit == null)
            retrofit = initializeRetrofit()
        return retrofit!!
    }

    private fun initializeRetrofit(): RetrofitInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiKeys.URL_API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }

    /**
     * Method used to create type token which provide Jamun Volley a format to create or parse json
     *
     * @param typeToken You need to type VolleyGSON.get().getTypeToken(new TypeToken<Model.Class or List></Model.Class><Model>>(){});
     * @return object
     * @author Jatin Sahgal (Jamun 01-12-2018)
    </Model> */
    fun getTypeToken(typeToken: TypeToken<*>): Type {
        return typeToken.type
    }

    /**
     * Method used to create JSON From Model or Collection Variable
     *
     * @param object Your Collection or model Object
     * @param type   You need to type VolleyGSON.get().getTypeToken(new TypeToken<Model.Class or List></Model.Class><Model>>(){});
     * @return String of json
    </Model> */
    fun toJson(`object`: Any?, type: Type?): String {
        return gson.toJson(`object`, type)
    }

    /**
     * Method used to parse your data into Your Model Object
     *
     * @param type You need to type VolleyGSON.get().getTypeToken(new TypeToken<Model.Class or List></Model.Class><Model>>(){});
     * @param data String of your JSON response
     * @return Return you Object of your Model Class
    </Model> */
    fun fromJson(type: Type?, data: String?): Any {
        return gson.fromJson(data, type)
    }

    companion object {
        /**
         * Get singleton implementation of Retrofit Util to access its Data Members [RetrofitInterface] and [Gson]
         *
         * @author Jatin Sahgal
         */
        @Volatile
        @get:Synchronized
        var instance: RetrofitUtil = RetrofitUtil()


    }
}