package com.example.linkswell.home.data.models.appmodel

data class LinkGroupCollectionAppModel(
    val groupName: String,
    val links: List<LinkAppModel>
)

fun List<LinkAppModel>.toLinkGroupCollectionAppModel(groupName: String?): LinkGroupCollectionAppModel {
    return LinkGroupCollectionAppModel(
        groupName = groupName ?: this.firstOrNull()?.groupName ?: "unknown group",
        links = this
    )
}
