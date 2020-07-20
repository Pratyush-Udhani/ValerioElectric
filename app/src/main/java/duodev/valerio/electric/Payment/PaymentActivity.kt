package duodev.valerio.electric.Payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.GeoPoint
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Payment.ViewModel.PaymentViewModel
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Station.StationSingleActivity
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.pojos.*
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject


class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private val pm = PreferenceUtils
    private lateinit var station: HashMap<String,Any>
    private lateinit var plug: Ports
    private var time: Long = 0
    private val paymentViewModel = PaymentViewModel()
    private var mode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        intent?.let {
            station = it.getSerializableExtra(StationSingleActivity.STATION)!! as HashMap<String,Any>
            plug = it.getParcelableExtra(PLUG)!!
            time = it.getLongExtra(TIME, 0)
        }
        init()
    }

    private fun init() {
        setUpListeners()
        setUpBookings()
    }

    private fun setUpBookings(): Bookings {
        return Bookings(
            Station(
                stationAddress = station[StationListFragment.ADDRESS].toString(),
                location = GeoPoint(station[StationListFragment.LATITUDE].toString().toDouble(), station[StationListFragment.LONGITUDE].toString().toDouble() ),
                stationLocation = station[StationListFragment.LOCATION].toString(),
                numberOfStations = station[StationListFragment.SLOTS].toString().toInt(),
                serviceProvider = station[StationListFragment.PROVIDER].toString(),
                imageUrl = station[StationListFragment.IMAGE_URL].toString(),
                stationId = station[StationListFragment.ID].toString(),
                ownerCompany = station[StationListFragment.OWNER] as Company,
                connectorType = station[StationListFragment.CONNECTOR] as List<Connector>,
                ownership = station[StationListFragment.OWNERSHIP].toString()
            ),
            Ports(
                portName = plug.portName,
                portCost = plug.portCost
            ),
            time = time,
            id = ""
        )
    }

    private fun setUpListeners() {
        paymentButton.setOnClickListener {
            if (mode != "") {
            razorPayPayment(mode, "20000")
                Log.d("TQTQ", pm.mobile)
            } else {
                this.toast("Select a method")
            }
        }

        payment_radio_group.setOnCheckedChangeListener { _,checkedId ->
            when(checkedId){
                (R.id.payment_debit) -> {
                    mode = "card"
                }
                (R.id.payment_credit) -> {
                    mode = "card"
                }
                (R.id.payment_paytm) -> {
                    mode = "upi"
                }
            }
        }
    }

    private fun razorPayPayment(method: String, amount: String) {
        val checkout = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Valerio Electric")
            options.put("description", "Payment for services")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", amount)
            options.put("send_sms_hash", true)
            val prefill = JSONObject()
            prefill.put("email", "valerioelectric20@gmail.com")
            prefill.put("contact", pm.mobile)
            prefill.put("method", method)
            options.put("prefill", prefill)
            log(method)
            checkout.open(this, options)
        } catch (e: Exception) {
            // Handle Exception
            //  System.out.println(e.getMessage())
        }
    }


    // should could be called after payment success
    private fun confirmBooking() {
        paymentViewModel.confirmBooking(setUpBookings())
        startActivity(HomeActivity.newInstance(this))
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        this.toast("payment  success")
    }

    companion object {

        private const val TIME = "time"
        private const val PLUG = "plug"

        fun newInstance(context: Context, station: HashMap<String,Any>, plug: Ports, time: Long) = Intent(context,PaymentActivity::class.java).apply {
                putExtra(StationSingleActivity.STATION, station)
                putExtra(PLUG, plug)
                putExtra(TIME, time)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        this.toast("Error: $p1")
    }

    override fun onPaymentSuccess(p0: String?) {
        confirmBooking()
    }

}