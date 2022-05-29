package com.example.linkswell.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkswell.home.HomeLinksIntent
import com.example.linkswell.home.HomeLinksPartialChange
import com.example.linkswell.home.HomeLinksSideEffect
import com.example.linkswell.home.HomeLinksViewState
import com.example.linkswell.home.domain.GetLinksGroupedInSameTypeListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeLinksViewModel @Inject constructor(
    private val getLinksGroupedInSameTypeListUseCase: GetLinksGroupedInSameTypeListUseCase
) : ViewModel() {

    private val sideEffectsChannel = Channel<HomeLinksSideEffect>(Channel.BUFFERED)
    val sideEffects: Flow<HomeLinksSideEffect> get() = sideEffectsChannel.receiveAsFlow()

    private val intents = MutableSharedFlow<HomeLinksIntent>()

    lateinit var viewState: StateFlow<HomeLinksViewState>
        private set

    init {
        viewModelScope.launch {
            viewState = intents
                .onEach { println("intent: $it") }
                .toPartialChange()
                .onEach { println("partial change: $it") }
                .toSideEffects()
                .scan(HomeLinksViewState()){ old, action -> action.reduce(old) }
                .stateIn(viewModelScope)
        }
    }

    suspend fun handleIntent(intent: HomeLinksIntent) = intents.emit(intent)

    private fun Flow<HomeLinksIntent>.toPartialChange(): Flow<HomeLinksPartialChange> {
        return merge(
            filterIsInstance<HomeLinksIntent.ViewCreated>()
                .let { handleViewCreatedIntent(it) },
            filterIsInstance<HomeLinksIntent.LinkGroupClicked>()
                .let { handleLinkGroupClickedIntent(it) }
        )
    }

    private fun Flow<HomeLinksPartialChange>.toSideEffects(): Flow<HomeLinksPartialChange> {
        return onEach {
            val effect = when(it) {
                is HomeLinksPartialChange.ViewCreated.Success -> null
                is HomeLinksPartialChange.ViewCreated.Loading -> null
                is HomeLinksPartialChange.LinkGroupClicked -> {
                    HomeLinksSideEffect.NavigateToGroupLinks(it.linkGroup)
                }
            }

            effect?.let {
                val res = sideEffectsChannel.trySend(effect)
            }
        }
    }

    private fun handleViewCreatedIntent(
        flow: Flow<HomeLinksIntent.ViewCreated>
    ): Flow<HomeLinksPartialChange.ViewCreated> {
        return flow.flatMapLatest {
            val loadingFlow = flowOf(HomeLinksPartialChange.ViewCreated.Loading)

            val resultFlow = getLinksGroupedInSameTypeListUseCase.invoke()
                .map {
                    HomeLinksPartialChange.ViewCreated.Success(it)
                }

            merge(loadingFlow, resultFlow)
        }
    }

    private fun handleLinkGroupClickedIntent(
        flow: Flow<HomeLinksIntent.LinkGroupClicked>
    ): Flow<HomeLinksPartialChange.LinkGroupClicked> {
        return flow.map {
            HomeLinksPartialChange.LinkGroupClicked(it.linkGroup)
        }
    }

}