package com.example.linkswell.savelink

import com.example.linkswell.mvi.PartialChange

data class SaveLinkViewState(
    val link: String? = null,
    val groupName: String? = null,
    val extraNote: String? = null,
    val groupNameError: String? = null,
    val shouldEnableSaveButtons: Boolean = false
)

sealed class SaveLinkIntent {
    data class OnViewCreated(val originalLink: String): SaveLinkIntent()
    data class OnGroupNameEdit(val newGroupName: String): SaveLinkIntent()
    data class OnExtraNoteEdit(val newExtraNote: String): SaveLinkIntent()
    data class OnSaveAndCloseClicked(val shouldCloseAfterSave: Boolean = false): SaveLinkIntent()
}

sealed class SaveLinkSideEffect {
    data class UpdateLinksFields(val link: String, val groupName: String, val extraNote: String?): SaveLinkSideEffect()
    data class ShowErrorAndCloseApp(val errorMsg: String) : SaveLinkSideEffect()
    object CloseApp : SaveLinkSideEffect()
}

sealed class SaveLinkPartialChange : PartialChange<SaveLinkViewState> {
    sealed class OnViewCreated : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkViewState): SaveLinkViewState {
            return when(this) {
                is Success -> {
                    oldState.copy(
                        link = this.link,
                        groupName = this.groupName,
                        extraNote = this.extraNote,
                        groupNameError = null,
                        shouldEnableSaveButtons = true
                    )
                }
                is Error -> {
                    oldState.copy(
                        link = null,
                        groupName = null,
                        extraNote = null,
                        groupNameError = null, // here passing null, because on viewcreated, if there is some issue the app will show the error and close directly
                        shouldEnableSaveButtons = false
                    )
                }
            }
        }

        data class Success(val link: String, val groupName: String, val extraNote: String?): OnViewCreated()
        data class Error(val errorMsg: String): OnViewCreated()
    }
    sealed class OnGroupNameEdit : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkViewState): SaveLinkViewState {
            return when(this) {
                is Success -> {
                    oldState.copy(
                        groupName = newGroupName,
                        groupNameError = null,
                        shouldEnableSaveButtons = true
                    )
                }
                is Error -> {
                    oldState.copy(
                        groupName = groupName,
                        groupNameError = errorMsg,
                        shouldEnableSaveButtons = false
                    )
                }
            }
        }

        data class Success(val newGroupName: String, val oldGroupName: String) : OnGroupNameEdit()
        data class Error(val groupName: String, val errorMsg: String): OnGroupNameEdit()
    }
    data class OnExtraNoteEdit(val extraNote: String?) : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkViewState): SaveLinkViewState {
            return oldState.copy(
                extraNote = extraNote
            )
        }
    }
    data class OnSaveAndCloseClicked(val shouldCloseAfterSave: Boolean) : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkViewState): SaveLinkViewState {
            return oldState
        }
    }
}

