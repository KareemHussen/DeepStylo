package com.example.deepstylo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object ApiClient {

   private var okHttpClient = OkHttpClient.Builder()
       .readTimeout(30, TimeUnit.MINUTES)
       .connectTimeout(30 , TimeUnit.MINUTES)
       .callTimeout(30 , TimeUnit.MINUTES)
       .writeTimeout(30 , TimeUnit.MINUTES)
       .build()

   private val retrofit by lazy {
       Retrofit.Builder()
           .baseUrl("http://144.217.15.17:8000/")
           .addConverterFactory(GsonConverterFactory.create())
           .client(okHttpClient)
           .build()
   }

   val api: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}