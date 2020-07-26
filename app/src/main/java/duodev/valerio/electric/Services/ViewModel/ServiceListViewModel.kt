package duodev.valerio.electric.Services.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Services.Repo.ServiceListRepo
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.pojos.ServiceStation
import duodev.valerio.electric.pojos.Station
import kotlinx.coroutines.launch

class ServiceListViewModel : ViewModel() {

    private val serviceListRepo = ServiceListRepo()

    private var _data = MutableLiveData<List<Station>>()
    val data: LiveData<List<Station>>
        get() = _data

    fun fetchData() = serviceListRepo.fetchData()

    fun fetchBookings() = serviceListRepo.fetchBookings()

    fun setUpBooking(serviceStation: ServiceStation, context: Context) = serviceListRepo.setUpBooking(serviceStation, context)
//    {
//
//        viewModelScope.launch {
//             stationListRepo.fetchData()
//        }
//    }
}