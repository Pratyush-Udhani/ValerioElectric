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

//        firestore.collection("Services").get().addOnSuccessListener {
//            for (i in 0 until it.documents.size) {
//                log(it.documents[i].data!!.toString())
//                serviceList.add(convertToPojo(it.documents[i].data!!, ServiceStation::class.java))
//            }
//            data.value = serviceList
//        }

        serviceList.add(
            ServiceStation(
                serviceName = "RE 50 Range Booster Pack ",
                serviceProvider = Company(
                    name = "Northway Motorsport",
                    imageUri = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/companyimage%2Fsg.jpg?alt=media&token=5666630d-e1fa-4c7e-a27f-60bb4ec366df"
                ),
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "118000",
                serviceImage = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/stationimage%2F.jpg?alt=media&token=d262a7c8-3c28-4827-beff-f944b78b1a85",
                id = "0",
                location = GeoPoint(18.6216333, 73.8247188),
                servicePhone = "7898164077",
                serviceEmail = "pratyush.udhani@gmail.com"
            )
        )

        serviceList.add(
            ServiceStation(
                serviceName = "RE 100 Range Booster Pack ",
                serviceProvider = Company(
                    name = "Northway Motorsport",
                    imageUri = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/companyimage%2Fsg.jpg?alt=media&token=5666630d-e1fa-4c7e-a27f-60bb4ec366df"
                ),
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "177000",
                serviceImage = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/stationimage%2F.jpg?alt=media&token=d262a7c8-3c28-4827-beff-f944b78b1a85",
                id = "1",
                location = GeoPoint(18.6216333, 73.8247188),
                servicePhone = "7898164077",
                serviceEmail = "pratyush.udhani@gmail.com"
            )
        )

        data.value = serviceList

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
                .withMailto(pm.email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Service Booked")
                .withBody(
                    """
                        Dear ${pm.name.split(" ")[0]}
                        
                        You have successfully booked ${service.serviceName} service with ${service.serviceProvider.name}.
                        Contact the provider at ${service.serviceEmail} or ${service.servicePhone}. 
                        To confirm the booking head on to the bookings page in the settings menu of your Valerio Electric App.
                         
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
                        .withMailto(service.serviceEmail)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Service Booked")
                        .withBody(
                            """
                        Dear ${service.serviceProvider.name}
                        
                        ${service.serviceName} has been booked by user ${pm.name}. Your contact details have been shared with them.
                        You can also contact them at ${pm.email} or ${pm.mobile}
                        
                        For any queries contact us at $ADMIN_EMAIL
                        Regards
                        Valerio Electric
                        """.trimIndent()
                        )
                        .withProcessVisibility(false)
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