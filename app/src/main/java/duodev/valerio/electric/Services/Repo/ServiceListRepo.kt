package duodev.valerio.electric.Services.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Utils.convertToPojo
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.pojos.Station

class ServiceListRepo {
    private val serviceList: MutableList<Station> = mutableListOf()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun fetchData(): LiveData<List<Station>> {
        val data = MutableLiveData<List<Station>>()
        firestore.collection("Stations").get().addOnCompleteListener { log("${it.result?.documents}") }
        firestore.collection("Stations").get().addOnSuccessListener {
            log("${it.documents}")
            for (i in 0 until it.documents.size) {
                log(it.documents[i].data!!.toString())
                serviceList.add(convertToPojo(it.documents[i].data!!, Station::class.java))
            }
            data.value = serviceList
        }

        //     data.value = stationList
        return data
    }

}