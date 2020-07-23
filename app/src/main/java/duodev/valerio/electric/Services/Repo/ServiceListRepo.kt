package duodev.valerio.electric.Services.Repo

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Utils.convertToPojo
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.ServiceStation
import duodev.valerio.electric.pojos.Station

class ServiceListRepo {
    private val serviceList: MutableList<ServiceStation> = mutableListOf()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                id = "",
                location = GeoPoint(18.6216333, 73.8247188)
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
                id = "",
                location = GeoPoint(18.6216333, 73.8247188)
            )
        )

        data.value = serviceList

        return data
    }

}