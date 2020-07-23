package duodev.valerio.electric.Services.Repo

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Utils.convertToPojo
import duodev.valerio.electric.Utils.log
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
                serviceProvider = "Northway Motorsport",
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "118000",
                serviceImage = "",
                id = "",
                location = GeoPoint(28.585611, 77.190806)
            )
        )

        serviceList.add(
            ServiceStation(
                serviceName = "RE 100 Range Booster Pack ",
                serviceProvider = "Northway Motorsport",
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "177000",
                serviceImage = "",
                id = "",
                location = GeoPoint(28.585611, 77.190806)
            )
        )

        data.value = serviceList

        return data
    }

}