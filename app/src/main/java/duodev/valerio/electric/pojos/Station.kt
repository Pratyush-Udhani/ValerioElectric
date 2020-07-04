package duodev.valerio.electric.pojos

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Station (
    @SerializedName("stationName")
    val stationName: String,

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
    val connectorType: @RawValue List<Connector>,

    @SerializedName("location")
    val location: @RawValue GeoPoint,

    @SerializedName("stationImage")
    val imageUrl: String,

    @SerializedName("stationId")
    val stationId: String

): Parcelable