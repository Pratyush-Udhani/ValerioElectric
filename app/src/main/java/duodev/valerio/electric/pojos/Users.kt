package duodev.valerio.electric.pojos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import duodev.valerio.electric.Utils.PreferenceUtils
import kotlinx.android.parcel.Parcelize

val pm = PreferenceUtils

@Parcelize
data class Users (

    @SerializedName("name")
    val name: String? = pm.name,

    @SerializedName("email")
    val email: String? = pm.email,

    @SerializedName("contact")
    val contact: String? = pm.mobile,

    @SerializedName("address")
    val address: String? = "",

    @SerializedName("imageUrl")
    val imageUrl: String? = "",

    @SerializedName("hash")
    val hash: String? = ""
) : Parcelable