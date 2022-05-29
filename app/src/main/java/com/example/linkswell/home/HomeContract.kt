package com.example.linkswell.home

import com.example.linkswell.home.data.models.appmodel.LinkGroupCollectionAppModel
import com.example.linkswell.mvi.PartialChange

data class HomeLinksViewState(
    val linkGroups: List<LinkGroupCollectionAppModel> = listOf(),
    val isLoading: Boolean = false,
    val shouldShowNoResultsMsg: Boolean = false
)

sealed class HomeLinksIntent {
    object ViewCreated : HomeLinksIntent()
    data class LinkGroupClicked(val linkGroup: LinkGroupCollectionAppModel) : HomeLinksIntent()
}

sealed class HomeLinksSideEffect {
    data class NavigateToGroupLinks(val linkGroup: LinkGroupCollectionAppModel) : HomeLinksSideEffect()
}

sealed class HomeLinksPartialChange : PartialChange<HomeLinksViewState> {
    sealed class ViewCreated : HomeLinksPartialChange() {
        override fun reduce(oldState: HomeLinksViewState): HomeLinksViewState {
            return when(this) {
                is Loading -> {
                    oldState.copy(
                        isLoading = true,
                        shouldShowNoResultsMsg = false
                    )
                }
                is Success -> {
                    oldState.copy(
                        isLoading = false,
                        linkGroups = this.linkGroups,
                        shouldShowNoResultsMsg = this.linkGroups.isEmpty()
                    )
                }
            }
        }

        object Loading: ViewCreated()
        data class Success(val linkGroups: List<LinkGroupCollectionAppModel>) : ViewCreated()
    }

    data class LinkGroupClicked(val linkGroup: LinkGroupCollectionAppModel) : HomeLinksPartialChange() {
        override fun reduce(oldState: HomeLinksViewState): HomeLinksViewState {
            return oldState
        }
    }
}