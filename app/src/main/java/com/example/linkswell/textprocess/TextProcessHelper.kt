package com.example.linkswell.textprocess

import android.util.Log
import android.webkit.URLUtil
import com.example.linkswell.home.data.models.appmodel.LinkAppModel
import com.example.linkswell.home.data.repository.ILinksRepository
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextProcessHelper @Inject constructor(
    private val linksRepository: ILinksRepository
){

    suspend fun processText(linkText: String): Boolean {
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

            linksRepository.insertLinkDetails(linkAppModel)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}