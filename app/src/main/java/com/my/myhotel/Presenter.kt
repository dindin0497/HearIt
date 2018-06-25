package com.my.myhotel

import android.util.Log
import com.my.myhotel.api.ApiClient
import com.my.myhotel.model.Hotel
import com.my.myhotel.model.HotelResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Presenter{

    val TAG =Presenter::class.java.simpleName

    private val hotels : MutableList<Hotel> = ArrayList()

    protected val apiClient: ApiClient = ApiClient()

    private var updateSet : MutableSet<onUpdateLisener> = HashSet()

    private var loading = false


    interface onUpdateLisener{
        fun onSuccess()
        fun onError(str: String )
    }

    fun loadHotel()
    {
        loading=true
        apiClient.service.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->onSuccess(result) },
                        { error -> onError(error) }
                )
    }

    fun isLoading() = loading

    fun register(listener: onUpdateLisener) = updateSet.add(listener)

    fun unRegister(listener: onUpdateLisener) = updateSet.remove(listener)

    fun getSize(): Int{
        return hotels.size
    }

    fun getItem(idx : Int): Hotel{
        return hotels[idx]
    }

    protected fun onSuccess(result: HotelResult){
        Log.d(TAG, "onSuccess")
        loading = false
        hotels.addAll(result.hotels)

        for ( listener in  updateSet)
            listener.onSuccess()
    }

    protected fun onError(result: Throwable){
        Log.d(TAG, "onError "+ result.toString())
        loading = false
        for ( listener in  updateSet)
            listener.onError(result.toString())
    }
}