package duodev.valerio.electric.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Connector (
    val type: String,
    val price: String
): Parcelable