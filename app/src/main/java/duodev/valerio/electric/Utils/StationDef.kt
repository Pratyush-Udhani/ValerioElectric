package duodev.valerio.electric.Utils

import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station

object StationDef {
    fun getStationList(): List<Station> {
        val list: MutableList<Station> = mutableListOf()
        val listOfNames = arrayListOf(
            "Chanakyapuri, New Delhi, Delhi 110021",
            "NDMC parking, opposite HDFC bank, Block M, Connaught Place, New Delhi, Delhi 110001",
            "Palika Kendra Parking, opp Jantar mantar, Sansad Marg, Police Colony, Connaught Place, New Delhi, Delhi 110001",
            "Janpath, Connaught Place, New Delhi, Delhi 110001",
            "Plaza Mall, Block H, Connaught Place, New Delhi, Delhi 110001"
        )

        val listOfLats = arrayListOf(
            28.585611,
            28.632806,
            28.627309,
            28.621592,
            28.635730
        )

        val listOnLongs = arrayListOf(
            77.190806,
            77.223,
            77.216573,
            77.218239,
            77.219663
        )

//        for (i in 0..4) {
//
//
//            list.add(
//                Station(
//                    stationAddress = listOfNames[i],
//                    location = GeoPoint(listOfLats[i], listOnLongs[i]),
//                    stationLocation = "Delhi",
//                    numberOfStations = 1,
//                    serviceProvider = "Delta Electronics",
//                    imageUrl = "asc",
//                    stationId = "",
//                    ownerCompany = Company(
//                        name = "NDMC",
//                        imageUri = "as"
//                    ),
//                    connectorType = listOf(
//                        Connector(
//                            type = TYPE_TWO,
//                            price = "Rs. 5.56 per hour"
//                        )
//                    ),
//                    ownership = "Private"
//                )
//            )
//        }

        return list
    }
}
