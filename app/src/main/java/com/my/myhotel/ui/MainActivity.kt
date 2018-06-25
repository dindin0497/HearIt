package com.my.myhotel.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.my.myhotel.App
import com.my.myhotel.presenter.Presenter
import com.my.myhotel.R
import com.my.myhotel.RecyclerAdapter
import com.my.myhotel.presenter.IPresenter

import kotlinx.android.synthetic.main.activity_main.*

/**
 * hotel list Activity
 */
class MainActivity : AppCompatActivity(), IPresenter.onUpdateLisener {
    val TAG = MainActivity::class.java.simpleName

    var adapter: RecyclerAdapter? = null

    var presenter: IPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.layoutManager = LinearLayoutManager(this)

        presenter = (application as App).presenter
        adapter = RecyclerAdapter(this, presenter!!)
        recycler.adapter = adapter

        if (presenter?.getSize()==0)
            presenter?.loadData()

    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG,"onResume")

        presenter?.register(this)

        val isloading = presenter?.isLoading()
        if (!isloading!!)
            loading.setVisibility(View.GONE)

    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG,"onPause")

        presenter?.unRegister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    override fun onSuccess() {
        adapter?.notifyDataSetChanged()
        loading.setVisibility(View.GONE)
    }

    override fun onError(str: String) {
        Log.d(TAG,"onError $str")
        loading.setVisibility(View.GONE)
        Toast.makeText(this,str, Toast.LENGTH_LONG).show()

    }
}
