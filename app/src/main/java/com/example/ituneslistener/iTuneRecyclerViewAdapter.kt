package com.example.ituneslistener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class iTuneRecyclerViewAdapter(data: List<SongData>, val listener: RecyclerViewClickListener): RecyclerView.Adapter<iTuneRecyclerViewAdapter.ViewHolder>() {
    var songs: List<SongData> = data //定義一個property去存data這個參數
    set(value) {
        field = value //把新的值儲存到backing field中（field是backing field的預設名稱）
        notifyDataSetChanged() //讓listview自動更新內容
    }

    interface RecyclerViewClickListener {
        fun onItemClick(view: View, position: Int)
    }

    public class ViewHolder (val view: View, val listener: RecyclerViewClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        override fun onClick(p0: View?) { //在view被點擊時通知listener
            listener.onItemClick(view, absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //當RecyclerView需要ViewHolder，就會呼叫此函式
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itune_list_item, parent, false) //載入layout

        val holder= ViewHolder(view, listener)
        view.setOnClickListener(holder)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //當RecyclerView需要ViewHolder去呈現資料時，就會呼叫此函式
        holder.imageView.setImageBitmap(songs[position].cover)
        holder.textView.text = songs[position].title
    }

    override fun getItemCount(): Int { //總共有幾筆資料
        return songs.size
    }

}