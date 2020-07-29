package duodev.valerio.electric.Utils

import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.ServiceStation

object ServiceDef {
    fun getServiceList() : List<ServiceStation> {
        val list : MutableList<ServiceStation> = mutableListOf()

        list.add(
            ServiceStation(
                serviceName = "RE 50 Range Booster Pack ",
                serviceProvider = Company(
                    name = "Northway Motorsport",
                    imageUri = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/companyimage%2Fsg.jpg?alt=media&token=5666630d-e1fa-4c7e-a27f-60bb4ec366df",
                    phone = "7898164077",
                    email = "pratyush.udhani@gmail.com"
                ),
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "118000",
                serviceImage = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/stationimage%2F.jpg?alt=media&token=d262a7c8-3c28-4827-beff-f944b78b1a85",
                id = "0",
                location = GeoPoint(18.6216333, 73.8247188),
                description = ""
            )

        )

        list.add(

            ServiceStation(
                serviceName = "RE 100 Range Booster Pack ",
                serviceProvider = Company(
                    name = "Northway Motorsport",
                    imageUri = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/companyimage%2Fsg.jpg?alt=media&token=5666630d-e1fa-4c7e-a27f-60bb4ec366df",
                    phone = "7898164077",
                    email = "pratyush.udhani@gmail.com"
                ),
                serviceAddress = "B - 5, Electronic Estate, MIDC, Bhosari, Pimpri-Chinchwad, Maharashtra 411026, India",
                servicePrice = "177000",
                serviceImage = "https://firebasestorage.googleapis.com/v0/b/valerio-ac7ff.appspot.com/o/stationimage%2F.jpg?alt=media&token=d262a7c8-3c28-4827-beff-f944b78b1a85",
                id = "1",
                location = GeoPoint(18.6216333, 73.8247188),
                description = ""
            )
        )

        return list
    }
}