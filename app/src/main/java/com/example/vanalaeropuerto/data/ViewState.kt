package com.example.vanalaeropuerto.data

sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    object Confirmed : ViewState()
    object Empty : ViewState()
    object Failure : ViewState()
    object InvalidParameters : ViewState()
}