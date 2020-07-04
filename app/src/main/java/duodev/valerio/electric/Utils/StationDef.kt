package duodev.valerio.electric.Utils

import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station

object StationDef {
    fun getStationList(): List<Station> {
        val list: MutableList<Station> = mutableListOf()
        for (i in 0..10) {
            list.add(

                Station(
                    stationName = "stationName $i",
                    stationAddress = "station Address $i",
                    stationId = "$i",
                    stationLocation = "",
                    numberOfStations = 4,
                    serviceProvider = "",
                    imageUrl = "asc",
                    location = GeoPoint(i*5.5, i*5.5),
                    ownerCompany = Company(
                        name = "company",
                        imageUri = "as"
                    ),
                    connectorType = arrayListOf(
                        Connector(
                            type = "Type 1"
                        )
                    )
                )
            )
        }
        return list
    }
}