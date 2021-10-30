package com.kc.coffer.androidDemo.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kc.coffer.CofferApplication
import com.kc.coffer.R
import com.kc.coffer.androidDemo.adapter.RcAdapter.*
import com.kc.coffer.model.BaseData
import com.nostra13.universalimageloader.core.ImageLoader

/**
 * author      : coffer
 * date        : 10/28/21
 * description :
 */
open class RcAdapter(private val mContext: Context) : RecyclerView.Adapter<RcViewHolder>() {

    private var mList: ArrayList<BaseData>? = null

    fun setData(list: ArrayList<BaseData>?) {
        this.mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.activity_view_main3_item, parent, false)
        return RcViewHolder(view)
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {
        mList?.let {
            holder.title.text = it[position].title
            ImageLoader.getInstance().displayImage(it[position].url, holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        mList?.let {
            return it.size
        }
        return 0
    }

    class RcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.t1)
        val imageView: ImageView = itemView.findViewById(R.id.img)
    }
}
