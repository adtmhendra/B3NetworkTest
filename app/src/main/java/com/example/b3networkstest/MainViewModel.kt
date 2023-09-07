package com.example.b3networkstest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<Resource<Int>>()
    val response: LiveData<Resource<Int>>
        get() = _response

    fun getDataAsync(
        name: String,
        job: String,
    ) {
        _response.value = (Resource.LOADING(true))
        viewModelScope.launch {
            try {
                val data = ApiConfig.apiEndpoint.getDataAsync(DataRequest(name, job))
                if (data.isSuccessful) {
                    _response.value = (Resource.SUCCESS(data.code()))
                    _response.value = (Resource.LOADING(false))
                } else {
                    _response.value = (Resource.LOADING(false))
                    _response.value = (Resource.EMPTY(true))
                }
            } catch (e: Exception) {
                _response.value = (Resource.FAILURE(e.message.toString()))
            }
        }
    }
}