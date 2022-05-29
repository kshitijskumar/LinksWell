package com.example.linkswell.home.data.models.appmodel

import com.example.linkswell.db.entity.LinkEntity

data class LinkAppModel(
    val originalLink: String,
    val timeStamp: Long,
    val groupName: String,
    val extraNote: String
)

fun List<LinkEntity>.toLinkAppModelsList(): List<LinkAppModel> {
    return this.map {
        it.toLinkAppModel()
    }
}

fun LinkEntity.toLinkAppModel(): LinkAppModel {
    return LinkAppModel(
        originalLink = originalLink,
        timeStamp = timestamp,
        groupName = groupName,
        extraNote = extraNote ?: ""
    )
}
