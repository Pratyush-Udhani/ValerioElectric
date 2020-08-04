package duodev.valerio.electric.Station.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.pojos.Station
import kotlinx.coroutines.launch

class StationListViewModel : ViewModel() {

    private val stationListRepo = StationListRepo()

    private var _data = MutableLiveData<List<Station>>()
    val data: LiveData<List<Station>>
        get() = _data

    fun fetchData() = stationListRepo.fetchData()

    fun fetchBookedData() = stationListRepo.fetchBookedData()
//    {
//
//        viewModelScope.launch {
//             stationListRepo.fetchData()
//        }
//    }
}