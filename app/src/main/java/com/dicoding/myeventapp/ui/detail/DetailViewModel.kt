package com.dicoding.myeventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.myeventapp.data.response.DetailEventResponse
import com.dicoding.myeventapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _eventDetail = MutableLiveData<DetailEventResponse>()
    val eventDetail: LiveData<DetailEventResponse> get() = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getEventDetail(eventId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<DetailEventResponse> = ApiConfig.getApiService().getDetailEvent(eventId.toString())
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}
