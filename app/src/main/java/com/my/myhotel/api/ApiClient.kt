package com.my.myhotel.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * http client
 */
class ApiClient {
    var service: ApiService

    init{
        val retrofit = Retrofit.Builder()
                .baseUrl("https://hipmunk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create(ApiService::class.java)
    }

}
