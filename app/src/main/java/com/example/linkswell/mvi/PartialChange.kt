package com.example.linkswell.mvi

interface PartialChange<ViewState> {
    fun reduce(oldState: ViewState): ViewState
}