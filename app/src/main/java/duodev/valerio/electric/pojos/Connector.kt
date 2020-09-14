package duodev.valerio.electric.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Connector (
    val type: String,
    val power: String,
    val price: String,
    val ports: String,
    val speed: String,
    val id: String
): Parcelable