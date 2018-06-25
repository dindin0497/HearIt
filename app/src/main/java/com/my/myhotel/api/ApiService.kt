package com.my.myhotel.api

import com.my.myhotel.model.Hotel
import com.my.myhotel.model.HotelResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface ApiService{
    @GET("mobile/coding_challenge")
    fun getList(): Observable<HotelResult>
}