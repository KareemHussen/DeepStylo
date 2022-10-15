package com.example.deepstylo.repository


import com.example.deepstylo.api.ApiClient
import com.example.deepstylo.model.ResponseBody
import com.example.deepstylo.api.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class Repository (private val api : ApiInterface) {
    suspend fun postImage(requestImage : MultipartBody.Part , style : RequestBody): retrofit2.Response<ResponseBody>{
        return api.postImage(requestImage , style)
    }
}