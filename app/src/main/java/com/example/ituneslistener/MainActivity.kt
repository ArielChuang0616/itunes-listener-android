package com.example.ituneslistener

import android.app.ListActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity(), iTuneRecyclerViewAdapter.RecyclerViewClickListener {
//    val titles = mutableListOf<String>() //用來記錄歌名
//    val adapter: ArrayAdapter<String> by lazy {
//        ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
//    }
//    val adapter: iTuneListViewAdapter by lazy {
//        iTuneListViewAdapter(this)
//    }
    val adapter: iTuneRecyclerViewAdapter by lazy {
        iTuneRecyclerViewAdapter(listOf<SongData>(), this) //空的歌曲清單
    }
    val swipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }

    private fun loadList() {
        iTuneSAX(object: ParserListener{ //用object作為語法
            override fun start() {
                swipeRefreshLayout.isRefreshing = true //出現畫面轉動的圖案
            }
            override fun finish(songs: List<SongData>) {
//                for (song in songs) {
//                    titles.add(song.title) //把每一首歌的歌名加到titles這個list裡面
//                }
//                adapter.notifyDataSetChanged() //呼叫adapter上面這個函式，讓adapter去更新list view上的內容
                adapter.songs = songs //把歌曲清單丟給adapter
                swipeRefreshLayout.isRefreshing = false //停止畫面轉動的圖案
            }
        }).parseURL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swiperefresh)
//        listAdapter = adapter //將list view的adapter設定為握們先前宣告的adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView) //取得recyclerView的reference
        recyclerView.adapter = adapter //設定他的adapter
        recyclerView.layoutManager = LinearLayoutManager(this) //設定layout manager，此處使用LinearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration( //設定recyclerView的裝飾
            recyclerView.context,
            DividerItemDecoration.HORIZONTAL
        ))

        swipeRefreshLayout.setOnRefreshListener {
//        titles.clear()
            loadList()
        }
        loadList()
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, adapter.songs[position].title, Toast.LENGTH_LONG).show()
    }
}