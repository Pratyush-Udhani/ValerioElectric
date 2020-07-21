package duodev.valerio.electric.Utils

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import duodev.valerio.electric.pojos.Users

private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
val isAuth = MutableLiveData<Boolean>(false)
val noPassword = MutableLiveData<Boolean>(false)
val noEmail = MutableLiveData<Boolean>(false)
val pm = PreferenceUtils

fun <T> convertToPojo(
    data: MutableMap<String, Any>,
    pojo: Class<T>
): T {
    val gson = Gson()
    data.toMap()
    val jsonElement = gson.toJsonTree(data)
    return gson.fromJson(jsonElement, pojo)
}

fun checkAuth(email: String, password: String) {
    isAuth.value = false
    noPassword.value = false
    noEmail.value = false
    val hash = generateHash(password)

    firebaseFirestore.collection(USERS).document(email).get()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.exists() && it.result!! != null) {
                    if (it.result!!.get("hash") == hash) {
                        isAuth.value = true
                        pm.setUser(convertToPojo(it.result!!.data!!, Users::class.java))
                    } else {
                        noPassword.value = true
                    }
                } else {
                    noEmail.value = true
                }
            }
        }
}

fun changePassword(password: String) {
    firebaseFirestore.collection(USERS).document(pm.email).update("hash", password)
}