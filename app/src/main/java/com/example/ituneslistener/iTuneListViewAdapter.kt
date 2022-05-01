package com.example.ituneslistener

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class iTuneListViewAdapter(val context: Context): BaseAdapter() {
    var songs: List<SongData> = listOf<SongData>()
    set(value) {
        field = value //把新的值儲存到backing field中（field是backing field的預設名稱）
        notifyDataSetChanged() //讓listview自動更新內容
    }

    override fun getCount(): Int { //回傳總共有多少筆資料
        return songs.size
    }

    override fun getItem(p0: Int): Any { //取得某一筆資料
        return songs[p0]
    }

    override fun getItemId(p0: Int): Long { //取得某一筆資料的Id，這邊會把item的index當作Id
        return p0.toLong() //將Int轉換為Long
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View { //取得Adapter要呈現的View
        // p0: 第幾筆資料
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater //inflater是一個系統的服務，需透過context來取得LayoutInflater的服務
        val itemView = inflater.inflate(R.layout.itune_list_item, null) //用inflater去載入itune_list_item這個layout
        val imageView = itemView.findViewById<ImageView>(R.id.imageView) //取得image view的reference
        imageView.setImageBitmap(songs[p0].cover) //設定圖片為專輯封面
        val textView = itemView.findViewById<TextView>(R.id.textView) //取得text view的reference
        textView.text = songs[p0].title //設定文字為歌名

        return itemView
    }
}