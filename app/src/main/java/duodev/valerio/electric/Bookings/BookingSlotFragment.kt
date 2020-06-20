package duodev.valerio.electric.Bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationSingleFragment
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.Utils.toast

class BookingSlotFragment : Fragment() {

    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressed + 2000 > System.currentTimeMillis()){
                replaceFragment(
                    this@BookingSlotFragment,
                    R.id.homeContainer,
                    StationSingleFragment.newInstance())
                } else {
                    activity?.toast("Press back again to cancel booking")
                }
            }
        })
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_slot, container, false)
    }

    companion object {
        fun newInstance() = BookingSlotFragment()
    }
}