package duodev.valerio.electric.Services.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Services.Repo.ServiceCompanyRepo
import duodev.valerio.electric.pojos.Company
import kotlinx.coroutines.launch

class ServiceCompanyViewModel: ViewModel() {
    private val serviceCompanyRepo =
        ServiceCompanyRepo()

    private var _data = MutableLiveData<List<Company>>()
    val data: MutableLiveData<List<Company>>
        get() = _data

    fun fetchData() {
        viewModelScope.launch {
            val response = serviceCompanyRepo.fetchData()
            if(response.value != null) {
                _data = response
            }
        }
    }
}