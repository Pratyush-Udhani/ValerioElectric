package duodev.valerio.electric.pojos

import com.google.gson.annotations.SerializedName

data class Station (

    @SerializedName("stationLocation")
    val stationLocation: String,

    @SerializedName("stationAddress")
    val stationAddress: String,

    @SerializedName("ownerCompany")
    val ownerCompany: Company,

    @SerializedName("serviceProvider")
    val serviceProvider: String,

    @SerializedName("numberOfStations")
    val numberOfStations: Int,

    @SerializedName("connectorType")
    val connectorType: List<Connector>,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)