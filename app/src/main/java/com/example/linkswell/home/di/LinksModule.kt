package com.example.linkswell.home.di

import com.example.linkswell.db.dao.LinksDao
import com.example.linkswell.home.data.datasource.ILocalLinksDataSource
import com.example.linkswell.home.data.datasource.LocalLinksDataSource
import com.example.linkswell.home.data.repository.ILinksRepository
import com.example.linkswell.home.data.repository.LinksRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LinksModule {

    @Singleton
    @Provides
    fun provideLocalLinksDataSource(linksDao: LinksDao): ILocalLinksDataSource = LocalLinksDataSource(linksDao)

    @Singleton
    @Provides
    fun provideLinksRepository(localDataSource: ILocalLinksDataSource): ILinksRepository = LinksRepository(localDataSource)

}