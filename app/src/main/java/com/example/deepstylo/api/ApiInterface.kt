package com.example.deepstylo.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("/")
    @Multipart
    suspend fun postImage(@Part image : MultipartBody.Part ,
                          @Part("style") style : RequestBody
                          ): Response<com.example.deepstylo.model.ResponseBody>


}