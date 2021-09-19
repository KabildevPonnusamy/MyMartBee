package com.mart.mymartbee.networking.retrofit;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

public class ApiClient {

    public static final String BASE_URL = "https://www.mymartbee.com/api/seller/";

    private static Retrofit retrofit = null;
    public static final int DEFAULT_TIMEOUT_SEC = 90;
    public static OkHttpClient client;

    public static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        }
        return retrofit;
    }

    public static Retrofit getClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("x-access-token","YOUR_ACCESS_TOKEN")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        //For logging the call on Logcat
        HttpLoggingInterceptor interceptor1 = new HttpLoggingInterceptor();
        interceptor1.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor1);
        client = httpClient.build();

        try {
            if (retrofit == null) {

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit;
    }
}
