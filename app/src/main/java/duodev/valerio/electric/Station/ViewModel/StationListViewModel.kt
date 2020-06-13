package duodev.valerio.electric.Station.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.pojos.Station
import kotlinx.coroutines.launch

class StationListViewModel : ViewModel() {

    private val stationListRepo = StationListRepo()

    private var _data = MutableLiveData<List<Station>>()
    val data: MutableLiveData<List<Station>>
        get() = _data

    fun fetchData() {
        viewModelScope.launch {
            val response = stationListRepo.fetchData()
            if (response.value != null) {
                _data = response
            }
        }
    }
}