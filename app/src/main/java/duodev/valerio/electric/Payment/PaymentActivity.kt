package duodev.valerio.electric.Payment

import android.R.id.message
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import duodev.valerio.electric.Bookings.BookingSlotFragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Payment.ViewModel.PaymentViewModel
import duodev.valerio.electric.R
import duodev.valerio.electric.Services.ServiceListFragment
import duodev.valerio.electric.Services.ServiceSingleActivity
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Station.StationSingleActivity
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseActivity
import duodev.valerio.electric.pojos.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.dialog_complete_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.math.ceil


class PaymentActivity : BaseActivity(), PaymentResultListener {

    private lateinit var station: HashMap<String, Any>
    private lateinit var serviceMap: HashMap<String, Any>
    private lateinit var service: ServiceStation
    private lateinit var stationBookings: Bookings
    private lateinit var plug: Connector
    private var time: Long = 0
    private val paymentViewModel = PaymentViewModel()
    private val detailsDialog by lazy { Dialog(this) }
    private var mode = ""
    private var duration = ""
    private var price = ""
    private var flag = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        intent?.let {
            flag = it?.getStringExtra(FLAG)!!
        }
        init()
    }

    private fun init() {
        handleFlag()
//        setUpListeners()

    }

    private fun handleFlag() {
        when (flag) {
            BookingSlotFragment.BOOKING_FLAG -> {
                intent?.let {
                    station =
                        it.getSerializableExtra(StationSingleActivity.STATION)!! as HashMap<String, Any>
                    plug = it.getParcelableExtra(PLUG)!!
                    time = it.getLongExtra(TIME, 0)
                    duration = it.getStringExtra(DURATION)!!
                    price = it.getStringExtra(PRICE)!!
                }
                stationBookings = setUpBookings()
                confirmBooking()

            }

            ServiceSingleActivity.BOOKING_FLAG -> {
                intent?.let {
                    serviceMap = it?.getSerializableExtra(SERVICE)!! as HashMap<String, Any>
                }
                service = setupService()
                confirmServiceBooking()
            }
        }
    }

    private fun setupService(): ServiceStation {
        return ServiceStation(
            serviceName = serviceMap[ServiceListFragment.NAME].toString(),
            serviceAddress = serviceMap[ServiceListFragment.ADDRESS].toString(),
            serviceProvider = serviceMap[ServiceListFragment.PROVIDER] as Company,
            serviceImage = serviceMap[ServiceListFragment.IMAGE_URL].toString(),
            servicePrice = serviceMap[ServiceListFragment.PRICE].toString(),
            location = GeoPoint(serviceMap[ServiceListFragment.LATITUDE] as Double, serviceMap[ServiceListFragment.LONGITUDE] as Double),
            id = serviceMap[ServiceListFragment.ID].toString(),
            description = serviceMap[ServiceListFragment.DESC].toString(),
            status = serviceMap[ServiceListFragment.STATUS].toString()
        )
    }

    private fun setUpBookings(): Bookings {
        return Bookings(
            Station(
                stationAddress = station[StationListFragment.ADDRESS].toString(),
                location = GeoPoint(
                    station[StationListFragment.LATITUDE].toString().toDouble(),
                    station[StationListFragment.LONGITUDE].toString().toDouble()
                ),
                stationLocation = station[StationListFragment.LOCATION].toString(),
                numberOfStations = station[StationListFragment.SLOTS].toString().toInt(),
                serviceProvider = station[StationListFragment.PROVIDER].toString(),
                imageUrl = station[StationListFragment.IMAGE_URL].toString(),
                stationId = station[StationListFragment.ID].toString(),
                ownerCompany = station[StationListFragment.OWNER] as Company,
                connectorType = station[StationListFragment.CONNECTOR] as List<Connector>,
                ownership = station[StationListFragment.OWNERSHIP].toString()
            ),
            Connector(
                type = plug.type,
                price = plug.price,
                id = plug.id
            ),
            time = time,
            id = "",
            duration = duration,
            price = "Rs. $price"
        )
    }

    private fun setUpListeners() {
        paymentButton.setOnClickListener {
            if (mode != "") {
                checkDetails()
            } else {
                this.toast("Select a method")
            }
        }

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        payment_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
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

    private fun checkDetails() {
            log(price)
            if (flag == ServiceSingleActivity.BOOKING_FLAG)
                razorPayPayment(mode, (ceil(service.servicePrice.toFloat()) * 100).toString())
            else
                razorPayPayment(mode, (ceil(price.toFloat()) * 100).toString())
        }

    private fun showDetailsDialog() {
        val view: View = layoutInflater.inflate(R.layout.dialog_complete_details, null)

        detailsDialog.apply {
            setContentView(view)

            create()
            detailsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            detailsDialog.setCanceledOnTouchOutside(true)
            detailsDialog.setCancelable(true)

            val phoneText: EditText = view.findViewById(R.id.phoneNumber)
            val address: EditText = view.findViewById(R.id.userAddress)
            val submitButton: CardView = view.findViewById(R.id.submitButton)
            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            phoneText.addTextChangedListener {
                if (phoneText.text.length == 10) {
                    closeKeyboard(this@PaymentActivity, phoneText)
                }
            }

            submitButton.setOnClickListener {
                if (phoneText.text.length == 10) {
                    progressBar.makeVisible()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            firebaseFirestore.collection(USERS).document(pm.email)
                                .update("contact", phoneText.text.toString()).addOnSuccessListener {
                                    firebaseFirestore.collection(USERS).document(pm.email)
                                        .update("address", address.text.toString())
                                }
                            return@withContext
                        }
                        return@launch
                    }
                    pm.mobile = phoneText.text.toString()
                    this@PaymentActivity.toast("Information Updated")
                    checkDetails()
                    dismiss()
                } else {
                    this@PaymentActivity.toast("Enter a valid phone number")
                }
            }

            show()
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
//        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 123)
//        } else {
//            val smsManager: SmsManager = SmsManager.getDefault()
//            smsManager.sendTextMessage(
//                "${stationBookings.station.ownerCompany.phone}",
//                null,
//                """
//                    Dear Provider
//                    ${stationBookings.plug.type} has been booked at ${long2time(stationBookings.time)} for duration
//                """.trimIndent(),
//                null,
//                null
//            ) // add the phone no of the service provider
            paymentViewModel.confirmBooking(stationBookings)
            startActivity(HomeActivity.newInstance(this, USER))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
            this.toast("payment  success")
    }

    private fun confirmServiceBooking() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 12)
        } else {
            val smsManagerProvider: SmsManager = SmsManager.getDefault()
            smsManagerProvider.sendTextMessage(
                pm.mobile,
                null,
                """
                    Greetings from VEcharge Bharat! Your booking has been confirmed. 
                    Service: ${service.serviceName}
                    Service Provider: ${service.serviceProvider.name}, ${service.serviceAddress}
                    Contact Details: ${service.serviceProvider.phone}
                    Looking forward to serving you again!
                """.trimIndent(),
                null,
                null
            )

            val smsManagerCust: SmsManager = SmsManager.getDefault()
            smsManagerCust.sendTextMessage(
                service.serviceProvider.phone,
                null,
                """
                    Greetings from VEcharge Bharat! Your service has received a booking. 
                    Service: ${service.serviceName}
                    Customer: ${pm.name}
                    Contact Details: ${pm.mobile}
                    Looking forward to serving you again!
                """.trimIndent(),
                null,
                null
            )
            paymentViewModel.confirmServiceBooking(service)
            startActivity(HomeActivity.newInstance(this, USER))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
            this.toast("payment  success")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 12) {
//            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val smsManager: SmsManager = SmsManager.getDefault()
//                smsManager.sendTextMessage("78981 64077", null, "message", null, null)
//                paymentViewModel.confirmServiceBooking(service)
//                startActivity(HomeActivity.newInstance(this, USER))
//                overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
//                this.toast("payment  success")
//            } else {
//                toast("Please grant permissions")
//            }
//        }

        if (requestCode == 12) {
            val smsManagerProvider: SmsManager = SmsManager.getDefault()
            smsManagerProvider.sendTextMessage(
                pm.mobile,
                null,
                """
                    Greetings from VEcharge Bharat! Your booking has been confirmed. 
                    Service: ${service.serviceName}
                    Service Provider: ${service.serviceProvider.name}, ${service.serviceAddress}
                    Contact Details: ${service.serviceProvider.phone}
                    Looking forward to serving you again!
                """.trimIndent(),
                null,
                null
            )

            val smsManagerCust: SmsManager = SmsManager.getDefault()
            smsManagerCust.sendTextMessage(
                service.serviceProvider.phone,
                null,
                """
                    Greetings from VEcharge Bharat! Your service has received a booking. 
                    Service: ${service.serviceName}
                    Customer: ${pm.name}
                    Contact Details: ${pm.mobile}
                    Looking forward to serving you again!
                """.trimIndent(),
                null,
                null
            )
            paymentViewModel.confirmServiceBooking(service)
            startActivity(HomeActivity.newInstance(this, USER))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
            this.toast("payment  success")
        } else {
            toast("Please grant permissions")
        }
    }

    companion object {

        private const val TIME = "time"
        private const val PLUG = "plug"
        private const val DURATION = "duration"
        private const val PRICE = "price"
        private const val FLAG = "flag"
        private const val SERVICE = "Service"

        fun newInstance(
            context: Context,
            station: HashMap<String, Any>,
            plug: Connector,
            time: Long,
            duration: String,
            price: String,
            flag: String
        ) = Intent(context, PaymentActivity::class.java).apply {
            putExtra(StationSingleActivity.STATION, station)
            putExtra(PLUG, plug)
            putExtra(TIME, time)
            putExtra(DURATION, duration)
            putExtra(PRICE, price)
            putExtra(FLAG, flag)
        }

        fun newInstance(context: Context, service: HashMap<String, Any>, flag: String) =
            Intent(context, PaymentActivity::class.java).apply {
                putExtra(SERVICE, service)
                putExtra(FLAG, flag)
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        this.toast("Error: $p1")
    }

    override fun onPaymentSuccess(p0: String?) {
        if (flag == ServiceSingleActivity.BOOKING_FLAG)
            confirmServiceBooking()
        else
            confirmBooking()
    }

}