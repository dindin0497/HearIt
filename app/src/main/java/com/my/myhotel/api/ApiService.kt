package com.my.myhotel.api

import com.my.myhotel.model.HotelResult
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * interface for http
 */
interface ApiService{

    /**
     * get hotel list
     */
    @GET("mobile/coding_challenge")
    fun getList(): Observable<HotelResult>
}