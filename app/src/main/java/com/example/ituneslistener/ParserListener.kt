package com.example.ituneslistener

interface ParserListener {
    //兩個function用來通知開始parse和結束parse的時候給observer
    //並且在結束parse的時候順便把parse到的歌曲傳給observer
    fun start()
    fun finish(songs: List<SongData>)
}