package com.my.myhotel.presenter

import com.my.myhotel.model.Hotel

/**
 * presenter interface
 */
interface IPresenter{

    interface onUpdateLisener{
        fun onSuccess()
        fun onError(str: String )
    }

    fun register(listener: onUpdateLisener): Boolean
    fun unRegister(listener: onUpdateLisener): Boolean

    fun loadData()
    fun isLoading(): Boolean
    fun getSize(): Int
    fun getItem(idx : Int): Hotel
}