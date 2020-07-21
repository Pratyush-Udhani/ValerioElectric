package duodev.valerio.electric.Bookings

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.Payment.PaymentActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Ports
import kotlinx.android.synthetic.main.fragment_booking_slot.*
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class BookingSlotFragment : BaseFragment() {

    private lateinit var plug: Ports
    private var arrivalTime: Long = 0
    private lateinit var station: Serializable

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
        setUpListeners()
    }

    private fun setUpListeners() {

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
                startActivity(PaymentActivity.newInstance(requireContext(), station as HashMap<String, Any>, plug, arrivalTime))
                activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
            } else {
                activity?.toast("Select arrival time")
            }
        }
    }

    companion object {

        private const val PLUG = "plug"
        private const val STATION = "Station"

        fun newInstance(plug: Ports, station: Serializable) = BookingSlotFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PLUG, plug)
                putSerializable(STATION, station)
            }
        }
    }
}