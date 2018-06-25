package com.my.myhotel

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import com.my.myhotel.ui.DetailActivity
import com.my.myhotel.presenter.IPresenter

/**
 * recyclerview Adapter
 */
class RecyclerAdapter (val context: Context, val presenter: IPresenter) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

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