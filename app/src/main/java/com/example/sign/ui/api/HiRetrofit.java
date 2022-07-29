package com.example.sign.ui.api;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class HiRetrofit extends Application {
        public static String header = "";
        public static String name = "";
        private static String url = "http://47.107.52.7:88/";
        public static String appId = "caedf3c6bd574f17a26c0d3384a48efd";
        public static String appSecret = "58825af8c3b7efc4149078e927b0612aac3aa";
        public static int id;


        public static OkHttpClient client  = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();

        public static retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
}

