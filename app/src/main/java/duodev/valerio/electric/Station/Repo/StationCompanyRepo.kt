package duodev.valerio.electric.Station.Repo

import androidx.lifecycle.MutableLiveData
import duodev.valerio.electric.Utils.Station_List
import duodev.valerio.electric.pojos.Company

class StationCompanyRepo {

    private var companyList: MutableList<Company> = mutableListOf()

    suspend fun fetchData(): MutableLiveData<List<Company>> {
        val data = MutableLiveData<List<Company>>()

        for(station in Station_List.distinctBy { it.ownerCompany.name }){
//            if(station.ownerCompany !in companyList){
            companyList.add(station.ownerCompany)
//            }
//            station.ownerCompany
        }

        data.value = companyList
        return data
    }
}