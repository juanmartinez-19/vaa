package com.example.vanalaeropuerto

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.TripsUiState
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConfirmedTripsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val tripsRepository: TripsRepository = mockk()
    private val requesterRepository: RequesterRepository = mockk()
    private val driversRepository: DriversRepository = mockk()

    private lateinit var viewModel: ConfirmedTripsViewModel

    @Before
    fun setup() {
        viewModel = ConfirmedTripsViewModel(
            tripsRepository,
            requesterRepository,
            driversRepository
        )
    }

    @Test
    fun `when repo returns empty list then state is Empty`() = runTest {
        // Arrange
        coEvery { tripsRepository.getConfirmedTrips() } returns
                MyResult.Success(mutableListOf())

        // Act
        viewModel.getConfirmedTrips()

        advanceUntilIdle()

        val state = viewModel.uiState.getOrAwaitValue()

        // Assert
        assertTrue(state is TripsUiState.Empty)
    }

    @Test
    fun `when repository returns trips, state is success`() = runTest {

        // Arrange
        val trips = mutableListOf(
            Trip(
                date = System.currentTimeMillis(),
                originAddress = "Origen",
                destinationAddress = "Destino",
                adults = 2,
                children = 0,
                babies = 0,
                luggageKg = 10f,
                price = 1500f,
                state = "CONFIRMED",
                tripId = "1",
                segmentId = "seg1",
                requesterId = "req1",
                driverId = "driver1"
            )
        )
        val mockRequester = Requester(
            requesterId = "req1",
            requesterName = "Juan",
            requesterSurname = "Perez",
            requesterPhoneNumber = "123456",
            requesterCuil = "20-123",
            requesterRole = "USER"
        )

        val mockDriver = Driver(
            driverId = "driver1",
            driverName = "Pedro",
            tieneButaca = true,
            driverPhoneNumber = "654321",
            driverCuil = "20-456",
            driverSurname = "Gomez"
        )

        coEvery { requesterRepository.getRequester(any()) } returns MyResult.Success(mockRequester)

        coEvery { driversRepository.getDriverById(any()) } returns MyResult.Success(mockDriver)
        coEvery { tripsRepository.getConfirmedTrips() } returns
                MyResult.Success(trips)

        viewModel.getConfirmedTrips()

        advanceUntilIdle()

        val state = viewModel.uiState.getOrAwaitValue()

        assertTrue(state is TripsUiState.Success)
    }

    @Test
    fun `when repository fails, state is error`() = runTest {

        coEvery { tripsRepository.getConfirmedTrips() } returns MyResult.Failure(Exception("error"))

        viewModel.getConfirmedTrips()

        advanceUntilIdle()

        val state = viewModel.uiState.getOrAwaitValue()

        assertTrue(state is TripsUiState.Error)
    }

}