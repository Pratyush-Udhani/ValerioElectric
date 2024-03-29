package duodev.valerio.electric.pojos

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ServiceStation (

    @SerializedName("serviceName")
    val serviceName: String,

    @SerializedName("serviceAddress")
    val serviceAddress: String,

    @SerializedName("serviceProvider")
    val serviceProvider: Company,

    @SerializedName("serviceImage")
    val serviceImage: String,

    @SerializedName("servicePrice")
    val servicePrice: String,

    @SerializedName("location")
    val location: @RawValue GeoPoint,

    @SerializedName("id")
    var id: String,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("description")
    var description: String

    ) : Parcelable