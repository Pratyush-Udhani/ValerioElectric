package duodev.valerio.electric.Station.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Utils.convertToPojo
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station

class StationListRepo {

    private val stationList: MutableList<Station> = mutableListOf()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun fetchData(): LiveData<List<Station>> {
        val data = MutableLiveData<List<Station>>()
        firestore.collection("Stations").get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    log(it.documents[i].data!!.toString())
                    stationList.add(convertToPojo(it.documents[i].data!!, Station::class.java))
                }
                data.value = stationList
        }

        data.value = stationList
        return data
    }

}