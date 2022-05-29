package com.example.linkswell.di

import android.content.Context
import com.example.linkswell.db.AppDatabase
import com.example.linkswell.db.dao.LinksDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideLinksDao(appDb: AppDatabase): LinksDao = appDb.linksDao()
}