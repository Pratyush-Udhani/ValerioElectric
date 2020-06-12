package duodev.valerio.electric.Station.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.pojos.Station

class StationListViewModel : ViewModel() {

    private val stationListRepo = StationListRepo()

    private var _data = MutableLiveData<List<Station>>()
    val data: MutableLiveData<List<Station>>
        get() = _data

    fun fetchData() {
        val response = stationListRepo.fetchData()
        if (response.value != null) {
            _data = response
        }
    }
}