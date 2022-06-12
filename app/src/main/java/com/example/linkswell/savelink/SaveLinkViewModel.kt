package com.example.linkswell.savelink

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkswell.textprocess.TextProcessHelper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SaveLinkViewModel @Inject constructor(
    private val textProcessHelper: TextProcessHelper
) : ViewModel() {

    val viewState: StateFlow<SaveLinkViewState>

    private val intentsFlow = MutableSharedFlow<SaveLinkIntent>()

    private val sideEffectChannel = Channel<SaveLinkSideEffect>(Channel.BUFFERED)

    val sideEffects: Flow<SaveLinkSideEffect> = sideEffectChannel.receiveAsFlow()

    init {
        val initialViewState = SaveLinkViewState()
        viewState = intentsFlow
            .onEach { Log.d("SaveLink", "intent: $it") }
            .toPartialChange()
            .onEach { Log.d("SaveLink", "change: $it") }
            .emitSideEffects()
            .scan(initialViewState) { oldState, change -> change.reduce(oldState) }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialViewState)
    }

    suspend fun processIntent(intent: SaveLinkIntent) = intentsFlow.emit(intent)


    private fun Flow<SaveLinkIntent>.toPartialChange(): Flow<SaveLinkPartialChange> {
        return merge(
            filterIsInstance<SaveLinkIntent.OnViewCreated>()
                .let { handleOnViewCreatedIntent(it) }
        )
    }

    private fun Flow<SaveLinkPartialChange>.emitSideEffects(): Flow<SaveLinkPartialChange> {
        return onEach {
            val effect = when(it) {
                is SaveLinkPartialChange.OnViewCreated.Error -> {
                    SaveLinkSideEffect.ShowErrorAndCloseApp(it.errorMsg)
                }
                is SaveLinkPartialChange.OnViewCreated.Success -> {
                    SaveLinkSideEffect.UpdateLinksFields(it.link, it.groupName, it.extraNote)
                }
                else -> null
            }

            effect?.let { sideEffectChannel.trySend(effect) }
        }
    }

    private fun handleOnViewCreatedIntent(
        flow: Flow<SaveLinkIntent.OnViewCreated>
    ): Flow<SaveLinkPartialChange.OnViewCreated> {
        return flow.map {
            val linkAppModel = textProcessHelper.processText(it.originalLink)
            linkAppModel?.let { linkModel ->
                SaveLinkPartialChange.OnViewCreated.Success(
                    link = linkModel.originalLink,
                    groupName = linkModel.groupName,
                    extraNote = linkModel.extraNote
                )
            } ?: run {
                SaveLinkPartialChange.OnViewCreated.Error("The following link is not a valid link.")
            }
        }
    }

}