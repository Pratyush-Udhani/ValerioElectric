package duodev.valerio.electric.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Bookings (
   var station: Station,
   var plugType: String,
   var time: Date,
   var id: String
) : Parcelable