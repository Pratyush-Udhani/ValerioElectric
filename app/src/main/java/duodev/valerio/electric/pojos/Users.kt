package duodev.valerio.electric.pojos

import com.google.gson.annotations.SerializedName

data class Users (

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("contact")
    val contact: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("imageUrl")
    val imageUrl: String
)