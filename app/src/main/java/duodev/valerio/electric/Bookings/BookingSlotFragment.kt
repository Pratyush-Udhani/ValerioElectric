package duodev.valerio.electric.Bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationSingleFragment
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.pojos.Ports
import duodev.valerio.electric.pojos.Station

class BookingSlotFragment : Fragment() {

    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            log(it.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_slot, container, false)
    }

    companion object {
        fun newInstance(station: Station,plug: Ports) = BookingSlotFragment()
        fun newInstance() = BookingSlotFragment()
    }
}