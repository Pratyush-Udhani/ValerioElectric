package duodev.valerio.electric.Payment.Repo

import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Utils.BOOKINGS
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.Utils.USERS
import duodev.valerio.electric.pojos.Bookings

class PaymentRepo {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val pm = PreferenceUtils

    suspend fun confirmBooking(bookings: Bookings) {
        val id = firebaseFirestore.collection(USERS).document(pm.email).collection(BOOKINGS).document().id
        bookings.id = id
        firebaseFirestore.collection(USERS).document(pm.email).collection(BOOKINGS).document(id).set(bookings)
    }
}