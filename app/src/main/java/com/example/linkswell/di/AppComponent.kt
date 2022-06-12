package com.example.linkswell.di

import android.content.Context
import com.example.linkswell.home.di.LinksModule
import com.example.linkswell.home.ui.MainActivity
import com.example.linkswell.savelink.SaveLinkFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, LinksModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        fun build(): AppComponent
    }

    fun viewModelsFactory(): ViewModelFactory

    fun inject(activity: MainActivity)
    fun inject(fragment: SaveLinkFragment)

}