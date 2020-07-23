package duodev.valerio.electric.Payment.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Payment.Repo.PaymentRepo
import duodev.valerio.electric.pojos.Bookings
import duodev.valerio.electric.pojos.ServiceStation
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val paymentRepo = PaymentRepo()

    fun confirmBooking(booking: Bookings) {
        viewModelScope.launch {
            paymentRepo.confirmBooking(booking)
        }
    }

    fun confirmServiceBooking(serviceStation: ServiceStation) {
        viewModelScope.launch {
            paymentRepo.confirmServiceBooking(serviceStation)
        }
    }

}