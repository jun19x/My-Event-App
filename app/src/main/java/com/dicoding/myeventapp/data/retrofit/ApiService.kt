package com.dicoding.myeventapp.data.retrofit

import com.dicoding.myeventapp.data.response.DetailEventResponse
import com.dicoding.myeventapp.data.response.EventResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("events")
    suspend fun getEvents(): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(@Path("id") id: String): Response<DetailEventResponse>
}
