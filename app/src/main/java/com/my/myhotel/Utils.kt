package com.my.myhotel

import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.support.v4.widget.CircularProgressDrawable.LARGE
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object Utils {

    internal val TAG = Utils::class.java.simpleName


    private val requestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)

    fun loadBmpWithLowPriority(context: Context, imageView: ImageView, url: String) {

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.setStyle(LARGE)
        circularProgressDrawable.start()

        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(requestOptions)
                .priority(Priority.NORMAL)
                .placeholder(circularProgressDrawable)
                .into(imageView)
    }

    fun loadBmpWithHighPriority(context: Context, imageView: ImageView, url: String) {

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.setStyle(LARGE)
        circularProgressDrawable.start()

        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(requestOptions)
                .priority(Priority.HIGH)
                .placeholder(circularProgressDrawable)
                .into(imageView)
    }


}
