package com.example.deepstylo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deepstylo.model.ResponseBody
import com.example.deepstylo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var mutableResultImage : MutableLiveData<ResponseBody> = MutableLiveData()


    fun postImage(requestImage: MultipartBody.Part , style : RequestBody){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("5555555" ,"ahhhhhhhh")

                val response = repository.postImage(requestImage , style)

                Log.d("5555555" ,response.code().toString())

                    withContext(Dispatchers.Main){
                        if (response.isSuccessful){
                            mutableResultImage.value = response.body()
                        } else {
                            mutableResultImage.value = ResponseBody("Error")
                        }
                    }

            } catch (e : Exception){
                withContext(Dispatchers.Main) {
                    mutableResultImage.value = ResponseBody("Error")
                }
            }





        }


    }


}