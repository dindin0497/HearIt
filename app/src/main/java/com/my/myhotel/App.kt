package com.my.myhotel

import android.app.Application
import com.my.myhotel.presenter.IPresenter
import com.my.myhotel.presenter.Presenter

class App : Application() {
    val presenter : IPresenter = Presenter()

}