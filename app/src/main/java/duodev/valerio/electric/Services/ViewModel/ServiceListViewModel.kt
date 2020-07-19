package duodev.valerio.electric.Services.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Services.Repo.ServiceListRepo
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.pojos.Station
import kotlinx.coroutines.launch

class ServiceListViewModel : ViewModel() {

    private val serviceListRepo = ServiceListRepo()

    private var _data = MutableLiveData<List<Station>>()
    val data: MutableLiveData<List<Station>>
        get() = _data

    fun fetchData() = serviceListRepo.fetchData()
//    {
//
//        viewModelScope.launch {
//             stationListRepo.fetchData()
//        }
//    }
}