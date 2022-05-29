package com.example.linkswell.home.data.repository

import com.example.linkswell.home.data.models.appmodel.LinkAppModel
import kotlinx.coroutines.flow.Flow

interface ILinksRepository {

    fun getAllLinks(): Flow<List<LinkAppModel>>
}