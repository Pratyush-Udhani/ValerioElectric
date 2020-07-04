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
                    stationAddress = "ascslidn",
                    stationId = "$i",
                    stationLocation = "",
                    numberOfStations = 4,
                    serviceProvider = "",
                    imageUrl = "asc",
                    location = GeoPoint(23.233, 23.233),
                    ownerCompany = Company(
                        name = "asc",
                        imageUri = "as"
                    ),
                    connectorType = arrayListOf(
                        Connector(
                            type = "asc"
                        )
                    )
                )
            )
        }
        return list
    }
}