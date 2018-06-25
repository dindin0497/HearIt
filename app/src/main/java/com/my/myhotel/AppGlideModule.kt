package com.my.myhotel

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class AppGlideModule  : AppGlideModule()
{
    val TAG = AppGlideModule::class.java.getSimpleName()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val maxBitmapMemoryCache = Runtime.getRuntime().maxMemory() / 8
        val totalMemory = Runtime.getRuntime().totalMemory()
        val freeMemory = Runtime.getRuntime().freeMemory()

        Log.d(TAG, "max mem $maxBitmapMemoryCache")

        builder.setMemoryCache(LruResourceCache(maxBitmapMemoryCache))
        builder.setBitmapPool(LruBitmapPool(maxBitmapMemoryCache))

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

    }
}
