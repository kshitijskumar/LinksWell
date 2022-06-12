package com.example.linkswell.home.data.datasource

import com.example.linkswell.db.entity.LinkEntity
import kotlinx.coroutines.flow.Flow

interface ILocalLinksDataSource {
    fun getAllLinks(): Flow<List<LinkEntity>>

    suspend fun insertLinkDetails(linkEntity: LinkEntity)
}