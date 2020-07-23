package duodev.valerio.electric.Bookings

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Payment.PaymentActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Connector
import kotlinx.android.synthetic.main.fragment_booking_slot.*
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class BookingSlotFragment : BaseFragment() {

    private lateinit var plug: Connector
    private var arrivalTime: Long = 0
    private lateinit var station: Serializable
    private var hourPrice: Float = 0f
    private var minPrice: Float = 0f
    private var price = ""
    private var duration = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            plug = it.getParcelable(PLUG)!!
            station = it.getSerializable(STATION)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_slot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
        setUpListeners()
    }

    private fun setUpUI() {
        chargingTimeHours.addTextChangedListener {
            if (chargingTimeHours.text.isNotEmpty()) {
                hourPrice = plug.price.toFloat() * Integer.parseInt(chargingTimeHours.text.toString())
                bookSlotButton.text = "Check"
                price = "-1"
                Log.d("MINI", "$hourPrice hour")
            }
        }

        chargingTimeMinutes.addTextChangedListener {
            if (chargingTimeMinutes.text.isNotEmpty()) {
                minPrice = (plug.price.toFloat() * Integer.parseInt(chargingTimeMinutes.text.toString())) / 60
                bookSlotButton.text = "Check"
                price = "-1"
                Log.d("MINI", "$minPrice min")
            }
        }
//        chargingTimeMinutes.doAfterTextChanged {
//            if (chargingTimeMinutes.text.isNotEmpty()) {
//            }
//        }
    }

    private fun setUpListeners() {

        slotLayout.setOnClickListener {
            chargingTimeHours.clearFocus()
            chargingTimeMinutes.clearFocus()
        }

        val calendar = Calendar.getInstance()
        val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            arrivalTime = calendar.timeInMillis
            arrivalTimeText.text = long2time(arrivalTime)
        }

        arrivalTimeLayout.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                timePickerListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        bookSlotButton.setOnClickListener {
            if (arrivalTime != 0L) {
                if (price != "" && chargingTimeMinutes.text.isNotEmpty() && chargingTimeHours.text.isNotEmpty() && price != "0") {
                    if (bookSlotButton.text == "Check") {
                        price =  roundOffTwoDigits(hourPrice + minPrice)!!
                        totalPrice.text = "Rs. $price"
                        bookSlotButton.text = "Next"
                    } else {
                    duration = "${chargingTimeHours.text} hrs: ${chargingTimeMinutes.text} min"
                    startActivity(
                        PaymentActivity.newInstance(
                            requireContext(),
                            station as HashMap<String, Any>,
                            plug,
                            arrivalTime,
                            duration,
                            price,
                            BOOKING_FLAG
                        )
                    )
                    activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
                    }
                } else {
                    activity?.toast("Select duration")
                }
            } else {
                activity?.toast("Select arrival time")
            }
        }

        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }

        chargingTimeHours.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                closeKeyboard(requireContext(), v)
        }

        chargingTimeMinutes.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                closeKeyboard(requireContext(), v)
        }
    }

    companion object {

        private const val PLUG = "plug"
        private const val STATION = "Station"
        const val BOOKING_FLAG = "StationBooking"

        fun newInstance(plug: Connector, station: Serializable) = BookingSlotFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PLUG, plug)
                putSerializable(STATION, station)
            }
        }
    }
}