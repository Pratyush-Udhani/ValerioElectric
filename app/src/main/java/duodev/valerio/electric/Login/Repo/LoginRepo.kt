package duodev.valerio.electric.Login.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Utils.USERS
import duodev.valerio.electric.pojos.Users

class LoginRepo {

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun setUser(user: Users) {
        firebaseFirestore.collection(USERS).document(user.email!!).set(user)
    }
}