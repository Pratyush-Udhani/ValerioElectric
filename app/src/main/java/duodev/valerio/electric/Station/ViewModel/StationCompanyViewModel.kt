package duodev.valerio.electric.Station.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Station.Repo.StationCompanyRepo
import duodev.valerio.electric.pojos.Company
import kotlinx.coroutines.launch

class StationCompanyViewModel: ViewModel() {
    private val stationCompanyRepo =
        StationCompanyRepo()

    private var _data = MutableLiveData<List<Company>>()
    val data: LiveData<List<Company>>
        get() = _data

    fun fetchData() {
        viewModelScope.launch {
            val response = stationCompanyRepo.fetchData()
            if(response.value != null) {
                _data = response
            }
        }
    }
}