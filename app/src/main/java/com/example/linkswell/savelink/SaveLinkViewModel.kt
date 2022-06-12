package com.example.linkswell.savelink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkswell.textprocess.TextProcessHelper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SaveLinkViewModel @Inject constructor(
    private val textProcessHelper: TextProcessHelper
) : ViewModel() {

    var viewState: StateFlow<SaveLinkViewState>

    private val intentsFlow = MutableSharedFlow<SaveLinkIntent>()

    private val sideEffectChannel = Channel<SaveLinkSideEffect>(Channel.BUFFERED)

    val sideEffects: Flow<SaveLinkSideEffect> = sideEffectChannel.receiveAsFlow()

    init {
        val initialViewState = SaveLinkViewState()
        viewState = intentsFlow
            .toPartialChange()
            .emitSideEffects()
            .scan(initialViewState) { oldState, change -> change.reduce(oldState) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialViewState)
    }

    suspend fun processIntent(intent: SaveLinkIntent) = intentsFlow.emit(intent)


    private fun Flow<SaveLinkIntent>.toPartialChange(): Flow<SaveLinkPartialChange> {
        return merge()
    }

    private fun Flow<SaveLinkPartialChange>.emitSideEffects(): Flow<SaveLinkPartialChange> {
        return onEach {

        }
    }


}