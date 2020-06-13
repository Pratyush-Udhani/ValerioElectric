package duodev.valerio.electric.Station.Repo

import androidx.lifecycle.MutableLiveData
import duodev.valerio.electric.pojos.Station

class StationListRepo {

    private val stationList: MutableList<Station> = mutableListOf()

    suspend fun fetchData(): MutableLiveData<List<Station>> {
        val data = MutableLiveData<List<Station>>()

        for (i in 0..10) {
            stationList.add(
                Station(
                    stationName = "Name$i",
                    stationAddress = "Addrres",
                    stationAvailability = true,
                    starred = true,
                    stationPort = "as"
                )
            )
        }
        data.value = stationList
        return data
    }

}