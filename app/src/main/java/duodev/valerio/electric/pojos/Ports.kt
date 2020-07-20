package duodev.valerio.electric.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ports (
    val portName: String,
    val portCost: String
) : Parcelable