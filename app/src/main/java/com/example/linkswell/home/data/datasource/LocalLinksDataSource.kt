package com.example.linkswell.home.data.datasource

import com.example.linkswell.db.dao.LinksDao
import com.example.linkswell.db.entity.LinkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalLinksDataSource @Inject constructor(
    private val linksDao: LinksDao
) : ILocalLinksDataSource {

    override fun getAllLinks(): Flow<List<LinkEntity>> {
        return linksDao.getAllLinks()
    }

    override suspend fun insertLinkDetails(linkEntity: LinkEntity) {
        linksDao.insertLinkEntity(linkEntity)
    }
}