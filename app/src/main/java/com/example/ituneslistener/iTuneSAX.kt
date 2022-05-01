package com.example.ituneslistener

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.net.URL
import javax.xml.parsers.SAXParserFactory

class iTuneSAX(val listener: ParserListener): DefaultHandler() {
    private val factory = SAXParserFactory.newInstance()
    private val parser = factory.newSAXParser()
    private var entryFound = false //是否找到了某首歌
    private var titleFound = false //是否找到了某首歌名
    private var imageFound = false //是否找到了某首歌的專輯封面
    private var element: String = "" //存放每個element的資料
    private var songTitle: String = "" //存放歌名
    private var songCover: Bitmap? = null //存放專輯封面
    private var data = mutableListOf<SongData>() //存放所有歌曲的一個List，資料型態都是SongData的物件

    fun parseURL(url: String) {
        listener.start()
        GlobalScope.launch() {
            try {
                val inputStream = URL(url).openStream()
                parser.parse(inputStream, this@iTuneSAX)
                withContext(Dispatchers.Main) {
                    listener.finish(data)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        super.startElement(uri, localName, qName, attributes)
        if (localName == "entry") { //因為iTunes的xml檔中是以entry作為每一首歌曲的標籤
            entryFound = true
        }
        if (entryFound) {
            if (localName == "title") {
                titleFound = true
            } else if (localName == "image" && attributes?.getValue("height") == "170") {
                imageFound = true
            }
        }
        element = "" //先將element清空
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        if (entryFound) {
            if (titleFound) {
                titleFound = false //將titleFound重置為false
                songTitle = element
                Log.i("Title: ", songTitle) //除錯，印出歌名
            } else if (imageFound) {
                val url = element //將圖片的url擷取下來
                val inputStream = URL(url).openStream()
                songCover = BitmapFactory.decodeStream(inputStream)
                imageFound = false
            }
        }
        if (localName == "entry") {
            entryFound = false //將entryFound重置為false
            data.add(SongData(songTitle, songCover)) //將這首歌名與照片包成一個SongData物件，並放進data這個List中
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        super.characters(ch, start, length)
        ch?.let { //如果ch變數不是null就執行這段程式碼，如果是null就不執行
            element += String(it, start, length) //it指的是前面的變數，此處即為ch
            //當資料完整讀取完畢，歌曲的名稱會存在element中
        }
    }
}

