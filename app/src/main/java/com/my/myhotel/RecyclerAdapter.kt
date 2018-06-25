package com.my.myhotel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import kotlinx.android.synthetic.main.list_item.view.*
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.my.myhotel.ui.DetailActivity
import kotlinx.android.synthetic.main.activity_detail.*


class RecyclerAdapter (val context: Context, val presenter: Presenter) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    val TAG=RecyclerAdapter::class.java.simpleName

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val word = view.tv_word
        val imgview=view.img
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        val item = presenter.getItem(position)

        holder.word.text=item.name

        Utils.loadBmpWithLowPriority(context,holder.imgview,item.image_url)

        holder.imgview.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(Constant.ITEM_POS, position)
                context.startActivity(intent)
            }
        })

    }

    override fun getItemCount(): Int {
        return presenter.getSize()
    }


}