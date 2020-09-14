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

        for (i in 0..4) {


            list.add(
                Station(
                    stationAddress = listOfNames[i],
                    location = GeoPoint(listOfLats[i], listOnLongs[i]),
                    stationLocation = "Delhi",
//                    numberOfStations = 1,
                    serviceProvider = "Delta Electronics",
                    imageUrl = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/stationimage%2F73.8247188.jpg?alt=media&token=56525f51-5deb-44d0-af50-3adfb6c907eb",
                    stationId = "$i",
                    ownerCompany = Company(
                        name = "NDMC",
                        imageUri = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/companyimage%2Fnsjs.jpg?alt=media&token=8f3f8136-9876-4960-b890-4f09f4800db9",
                        email = "pratyush.udhani@gmail.com",
                        phone = "7898164077"
                    ),
                    connectorType = listOf(
                        Connector(
                            type = TYPE_TWO,
                            price = "Rs. 5.56 per hour",
                            id = (i+1).toString(),
                            power = "50",
                            ports = "1",
                            speed = "Fast"
                        )
                    ),
                    ownership = "Private"
                )
            )
        }

        return list
    }
}
