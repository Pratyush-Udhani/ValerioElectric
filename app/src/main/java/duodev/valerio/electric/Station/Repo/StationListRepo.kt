package duodev.valerio.electric.Station.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Utils.convertToPojo
import duodev.valerio.electric.pojos.Station

class StationListRepo {

    private val stationList: MutableList<Station> = mutableListOf()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun fetchData(): MutableLiveData<List<Station>> {
        val data = MutableLiveData<List<Station>>()

        firestore.collection("Stations").get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (i in 0 until it.result!!.documents.size) {
                    stationList.add(convertToPojo(it.result!!.documents[i].data!!, Station::class.java) as Station)
                }
                data.value = stationList
            }
        }
        return data
    }

}