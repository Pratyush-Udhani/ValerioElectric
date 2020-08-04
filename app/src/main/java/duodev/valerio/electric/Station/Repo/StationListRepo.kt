package duodev.valerio.electric.Station.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Bookings
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station

class StationListRepo {

    private val stationList: MutableList<Station> = mutableListOf()
    private val bookingList: MutableList<Bookings> = mutableListOf()
    private val pm = PreferenceUtils
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun fetchData(): LiveData<List<Station>> {
        val data = MutableLiveData<List<Station>>()
        firestore.collection("Stations").get().addOnCompleteListener { log("${it.result?.documents}") }
        firestore.collection("Stations").get().addOnSuccessListener {
            log("${it.documents}")
                for (i in 0 until it.documents.size) {
                    log(it.documents[i].data!!.toString())
                    stationList.add(convertToPojo(it.documents[i].data!!, Station::class.java))
                }
                data.value = stationList
        }

   //     data.value = stationList
        return data
    }

    fun fetchBookedData(): LiveData<List<Bookings>> {
        val data = MutableLiveData<List<Bookings>>()
        firestore.collection(USERS).document(pm.email).collection(BOOKINGS).get().addOnSuccessListener {
            log("${it.documents}")
            for (i in 0 until it.documents.size) {
                Log.d("PK",it.documents[i].data!!.toString())
                bookingList.add(convertToPojo(it.documents[i].data!!, Bookings::class.java))
            }
            data.value = bookingList
        }

        return data
    }

}