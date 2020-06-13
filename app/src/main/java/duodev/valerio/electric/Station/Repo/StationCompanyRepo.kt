package duodev.valerio.electric.Station.Repo

import androidx.lifecycle.MutableLiveData
import duodev.valerio.electric.pojos.Company

class StationCompanyRepo {

    private var companyList: MutableList<Company> = mutableListOf()

    suspend fun fetchData(): MutableLiveData<List<Company>> {
        var data = MutableLiveData<List<Company>>()

        for (i in 0..10) {
            companyList.add(
                Company(
                    name = "comapny$i",
                    imageUri = "asc"
                )
            )
        }

        data.value = companyList
        return data
    }
}