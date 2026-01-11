
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip

data class TripItemUI(
    private var trip: Trip,
    private var requester:  Requester,
    private var driver : Driver?
) {


    fun getTrip(): Trip {
        return this.trip
    }

    fun getRequester(): Requester {
        return this.requester
    }
    fun getDriver(): Driver? {
        return this.driver
    }

}