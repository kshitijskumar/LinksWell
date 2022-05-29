package com.example.linkswell.home.data.repository

import com.example.linkswell.home.data.datasource.ILocalLinksDataSource
import com.example.linkswell.home.data.models.appmodel.LinkAppModel
import com.example.linkswell.home.data.models.appmodel.toLinkAppModelsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LinksRepository @Inject constructor(
    private val localLinksDataSource: ILocalLinksDataSource
) : ILinksRepository {

    override fun getAllLinks(): Flow<List<LinkAppModel>> {
        return localLinksDataSource.getAllLinks().map { it.toLinkAppModelsList() }
    }
}