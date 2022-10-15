package com.example.deepstylo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.deepstylo.repository.Repository

class ViewModelFactory(private val repository: Repository):ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ViewModel(repository) as T
    }
}