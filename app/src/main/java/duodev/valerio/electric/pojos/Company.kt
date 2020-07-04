package duodev.valerio.electric.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Company(
    val name: String,
    val imageUri: String
): Parcelable