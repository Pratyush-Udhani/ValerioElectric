package duodev.valerio.electric.Payment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationSingleActivity
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.makeGone
import duodev.valerio.electric.Utils.makeVisible
import duodev.valerio.electric.pojos.Bookings
import duodev.valerio.electric.pojos.Ports
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.activity_payment.*
import java.util.*


class PaymentActivity : AppCompatActivity() {

    private var pm = PreferenceUtils
    private lateinit var station: Station

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("flag"))
        init()

    }

    private fun init() {
        setUpListeners()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            station = intent?.getParcelableExtra(StationSingleActivity.STATION)!!
            log("called recieve")
        }

    }

    private fun setUpListeners() {
        paymentButton.setOnClickListener {
            Log.d("TETETE", station.stationAddress)
        }

//        payment_radio_group.setOnCheckedChangeListener { _,checkedId ->
//            when(checkedId){
//                (R.id.payment_debit) -> {
//                    Log.d("Debit", "onCreate: debit card checked ")
//                    layout_debit_details.makeVisible()
//                    layout_credit_details.makeGone()
//                    layout_paytm_details.makeGone()
//                }
//
//                (R.id.payment_credit) -> {
//                    Log.d("Credit", "onCreate: credit card checked ")
//                    layout_debit_details.makeGone()
//                    layout_credit_details.makeVisible()
//                    layout_paytm_details.makeGone()
//
//                }
//                (R.id.payment_paytm) -> {
//                    Log.d("Paytm", "onCreate: paytm card checked ")
//                    layout_debit_details.makeGone()
//                    layout_credit_details.makeGone()
//                    layout_paytm_details.makeVisible()
//
//                }
//            }
//
//
//        }
    }


//    private fun uploadToFirebase() {
//        var firestoreFirebase = FirebaseFirestore.getInstance()
//        var id = firestoreFirebase.collection("Users").document(pm.email).collection("Bookings").document().id
//        booking.id = id
//        firestoreFirebase.collection("Users").document(pm.email)
//            .collection("Bookings")
//            .document(id)
//            .set(booking)
//            .addOnSuccessListener {
//                log("Booking added Successfully")
//            }
//    }

    companion object {
        fun newInstance(context: Context) = Intent(context, PaymentActivity::class.java)
    }

}