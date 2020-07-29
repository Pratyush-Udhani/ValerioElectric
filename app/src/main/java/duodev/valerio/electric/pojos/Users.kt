package duodev.valerio.electric.pojos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.Utils.generateHash
import kotlinx.android.parcel.Parcelize

val pm = PreferenceUtils

@Parcelize
data class Users (

    @SerializedName("name")
    var name: String? = pm.name,

    @SerializedName("email")
    val email: String? = pm.email,

    @SerializedName("contact")
    var contact: String? = pm.mobile,

    @SerializedName("address")
    var address: String? = "",

    @SerializedName("imageUrl")
    val imageUrl: String? = "",

    // def pass 0000
    @SerializedName("hash")
    val hash: String? = "9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0"
) : Parcelable