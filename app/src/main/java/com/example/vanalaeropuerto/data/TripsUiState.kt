package com.example.vanalaeropuerto.data

import TripItemUI

sealed class TripsUiState {

    object Loading : TripsUiState()

    object Empty : TripsUiState()

    data class Success(
        val trips: List<TripItemUI>
    ) : TripsUiState()

    data class Error(
        val message: String
    ) : TripsUiState()
}