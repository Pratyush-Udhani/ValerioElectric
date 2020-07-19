package duodev.valerio.electric.Payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Bookings
import duodev.valerio.electric.pojos.Ports
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.activity_payment.*
import java.util.*


class Payment : AppCompatActivity() {

    private var pm = PreferenceUtils
    private lateinit var booking: Bookings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
//        Log.d("TAG", "onCreate: Preference Utiles: $pm")
        intent.let {
            booking.station = it.getSerializableExtra("station") as Station
            booking.plugType = it.getStringExtra("port") as String
            booking.time = it.getSerializableExtra("date") as Date
        }
        init()

    }

    private fun init() {
        visibilityOffAll()
        setUpListeners()
    }

    private fun setUpListeners() {
        payment_radio_group.setOnCheckedChangeListener { _,checkedId ->
            when(checkedId){
                (R.id.payment_debit) -> {
                    Log.d("Debit", "onCreate: debit card checked ")
                    layout_debit_details.makeVisible()
                    layout_credit_details.makeGone()
                    layout_paytm_details.makeGone()
                }

                (R.id.payment_credit) -> {
                    Log.d("Credit", "onCreate: credit card checked ")
                    layout_debit_details.makeGone()
                    layout_credit_details.makeVisible()
                    layout_paytm_details.makeGone()

                }
                (R.id.payment_paytm) -> {
                    Log.d("Paytm", "onCreate: paytm card checked ")
                    layout_debit_details.makeGone()
                    layout_credit_details.makeGone()
                    layout_paytm_details.makeVisible()

                }
            }


        }
    }

    private fun visibilityOffAll() {
        layout_debit_details.makeGone()
        layout_credit_details.makeGone()
        layout_paytm_details.makeGone()
    }

    private fun uploadToFirebase() {
        var firestoreFirebase = FirebaseFirestore.getInstance()
        var id = firestoreFirebase.collection("Users").document(pm.email).collection("Bookings").document().id
        booking.id = id
        firestoreFirebase.collection("Users").document(pm.email)
            .collection("Bookings")
            .document(id)
            .set(booking)
            .addOnSuccessListener {
                log("Booking added Successfully")
            }
    }

}