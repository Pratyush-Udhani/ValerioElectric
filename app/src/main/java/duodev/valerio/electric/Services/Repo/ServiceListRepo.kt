package duodev.valerio.electric.Services.Repo

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.ServiceStation
import duodev.valerio.electric.pojos.Station

class ServiceListRepo {
    private val serviceList: MutableList<ServiceStation> = mutableListOf()
    private val bookingsList: MutableList<ServiceStation> = mutableListOf()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val pm = PreferenceUtils

     fun fetchData(): LiveData<List<ServiceStation>> {

        val data = MutableLiveData<List<ServiceStation>>()

        firestore.collection(SERVICES).get().addOnSuccessListener {
            for (i in 0 until it.documents.size) {
                log("called")
                log(it.documents[i].data!!.toString())
                serviceList.add(convertToPojo(it.documents[i].data!!, ServiceStation::class.java))
            }
            data.value = serviceList
        }
        return data
    }

    fun setUpBooking(serviceStation: ServiceStation, context: Context) {
        serviceStation.status = "booked"
        firestore.collection(USERS).document(pm.email).collection(SERVICE_BOOKINGS).document(serviceStation.id).set(serviceStation)
        sendMail(context, serviceStation)
    }

        private fun sendMail(
            context: Context,
            service: ServiceStation
        ) {
            BackgroundMail.newBuilder(context)
                .withUsername(ADMIN_EMAIL)
                .withPassword(ADMIN_PASS)
                .withSenderName("VEcharge Bharat")
                .withMailTo(pm.email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Service Booked")
                .withBody(
                    """
                        Dear ${pm.name.split(" ")[0]}
                        
                        You have successfully booked ${service.serviceName} service with ${service.serviceProvider.name}.
                        Contact the provider at ${service.serviceProvider.email} or ${service.serviceProvider.phone}. 
                        To confirm the booking head on to the bookings page in the settings menu of your Valerio Electric App.
                         
                        For any queries contact us at $ADMIN_EMAIL
                        Regards
                        VEcharge Bharat
                        """.trimIndent()
                )
//                .withProcessVisibility(false)
                .send()

                BackgroundMail.newBuilder(context)
                        .withUsername(ADMIN_EMAIL)
                        .withPassword(ADMIN_PASS)
                        .withSenderName("VEcharge Bharat")
                        .withMailTo(service.serviceProvider.email)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Service Booked")
                        .withBody(
                            """
                        Dear ${service.serviceProvider.name}
                        
                        ${service.serviceName} has been booked by user ${pm.name}. Your contact details have been shared with them.
                        You can also contact them at ${pm.email} or ${pm.mobile}
                        
                        For any queries contact us at $ADMIN_EMAIL
                        Regards
                        VEcharge Bharat
                        """.trimIndent()
                        )
//                        .withProcessVisibility(false)
                        .send()
    }

    fun fetchBookings(): LiveData<List<ServiceStation>> {
        val data = MutableLiveData<List<ServiceStation>>()
        firestore.collection(USERS).document(pm.email).collection(SERVICE_BOOKINGS).get().addOnSuccessListener {
            for (i in 0 until it.documents.size) {
                log(it.documents[i].data!!.toString())
                    bookingsList.add(convertToPojo(it.documents[i].data!!, ServiceStation::class.java))
            }
            data.value = bookingsList
        }
        return data
    }

}