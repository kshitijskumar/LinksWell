package com.example.linkswell.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkswell.home.ui.HomeLinksViewModel
import com.example.linkswell.savelink.SaveLinkViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeLinksViewModel::class)
    abstract fun bindHomeLinksViewModel(viewModel: HomeLinksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SaveLinkViewModel::class)
    abstract fun bindSaveLinkViewModel(viewModel: SaveLinkViewModel): ViewModel
}