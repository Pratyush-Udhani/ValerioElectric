package duodev.valerio.electric.Payment.Repo

import android.content.Context
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Bookings
import duodev.valerio.electric.pojos.ServiceStation

class PaymentRepo {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val pm = PreferenceUtils

    suspend fun confirmBooking(bookings: Bookings, context: Context) {
        val id = firebaseFirestore.collection(USERS).document(pm.email).collection(BOOKINGS).document().id
        bookings.id = id
        firebaseFirestore.collection(USERS).document(pm.email).collection(BOOKINGS).document(id).set(bookings)
        sendMail(context, bookings)
    }

    suspend fun confirmServiceBooking(serviceStation: ServiceStation) {
        firebaseFirestore.collection(USERS).document(pm.email).collection(SERVICE_BOOKINGS).document(serviceStation.id).update("status", "paid")
    }

    private fun sendMail(
        context: Context,
        stationBooking: Bookings
    ) {
        BackgroundMail.newBuilder(context)
            .withUsername(ADMIN_EMAIL)
            .withPassword(ADMIN_PASS)
            .withMailto(pm.email)
            .withType(BackgroundMail.TYPE_PLAIN)
            .withSubject("Service Booked")
            .withBody(
                """
                        Dear ${pm.name.split(" ")[0]}
                        
                        You have successfully booked ${stationBooking.station.ownerCompany.name} station at ${long2time(stationBooking.time)} for duration ${stationBooking.duration}.
                        The address is ${stationBooking.station.stationAddress}. 
                        
                        For any queries contact us at $ADMIN_EMAIL
                        Regards
                        Valerio Electric
                        """.trimIndent()
            )
            .withProcessVisibility(false)
            .send()

        BackgroundMail.newBuilder(context)
            .withUsername(ADMIN_EMAIL)
            .withPassword(ADMIN_PASS)
            .withMailto(stationBooking.station.ownerCompany.email)
            .withType(BackgroundMail.TYPE_PLAIN)
            .withSubject("Service Booked")
            .withBody(
                """
                        Dear ${stationBooking.station.ownerCompany.name}
                        
                        ${stationBooking.station.ownerCompany.name} charger number ${stationBooking.plug.id} with plug type ${stationBooking.plug.type} has been booked by ${pm.email}. 
                        The arrival time is ${long2time(stationBooking.time)} for duration ${stationBooking.duration}.
                         
                        For any queries contact us at $ADMIN_EMAIL
                        Regards
                        Valerio Electric
                        """.trimIndent()
            )
            .withProcessVisibility(false)
            .send()
    }

}