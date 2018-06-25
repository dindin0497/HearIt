package com.my.myhotel.presenter

import android.util.Log
import com.my.myhotel.api.ApiClient
import com.my.myhotel.model.Hotel
import com.my.myhotel.model.HotelResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * presenter implementer
 * connect view and model
 */
class Presenter : IPresenter{

    val TAG = Presenter::class.java.simpleName

    private val hotels : MutableList<Hotel> = ArrayList()

    protected val apiClient: ApiClient = ApiClient()

    private var updateSet : MutableSet<IPresenter.onUpdateLisener> = HashSet()

    private var loading : Boolean = false


    /**
     * load hotel data
     */
    override fun loadData()
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

    /**
     * is loading data
     */
    override fun isLoading() = loading

    /**
     * register for data load observer
     */
    override fun register(listener: IPresenter.onUpdateLisener) = updateSet.add(listener)

    /**
     * unregister for data load observer
     */
    override fun unRegister(listener: IPresenter.onUpdateLisener) = updateSet.remove(listener)

    /**
     * get hotel list size
     */
    override fun getSize(): Int{
        return hotels.size
    }

    /**
     * get a hotel item
     */
    override fun getItem(idx : Int): Hotel{
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