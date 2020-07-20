package duodev.valerio.electric.pojos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Bookings (

   @SerializedName("station")
   var station: Station,

   @SerializedName("plug")
   var plug: Ports,

   @SerializedName("time")
   var time: Long,

   @SerializedName("id")
   var id: String

) : Parcelable