package com.example.linkswell.textprocess

import android.util.Log
import android.webkit.URLUtil
import com.example.linkswell.home.data.models.appmodel.LinkAppModel
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextProcessHelper @Inject constructor(){

    suspend fun processText(linkText: String): LinkAppModel? {
        return try {
            val isValidUrl = URLUtil.isValidUrl(linkText)
            Log.d("TextStuff", "is valid $isValidUrl")

            if (!isValidUrl) throw IllegalArgumentException("$linkText is not a valid url")

            val uriLink = URI(linkText)
            val host = uriLink.host
            val hostWithoutWWWOrCom = host
                .replace("www.", "", true)
                .replace(".com", "", true)

            Log.d("TextStuff", "host $hostWithoutWWWOrCom")


            val linkAppModel = LinkAppModel(
                originalLink = linkText,
                timeStamp = System.currentTimeMillis(),
                groupName = hostWithoutWWWOrCom,
                extraNote = ""
            )

            Log.d("TextStuff", "app model $linkAppModel")

            linkAppModel
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}