package com.example.linkswell.di

import android.content.Context
import com.example.linkswell.home.di.LinksModule
import com.example.linkswell.home.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, LinksModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

}