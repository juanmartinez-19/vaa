package com.example.vanalaeropuerto.data

sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    object Empty : ViewState()
    object Failure : ViewState()
    object InvalidParameters : ViewState()
}