package duodev.valerio.electric.Station

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.Bookings.BookingPlugsFragment
import duodev.valerio.electric.Home.HomeMapFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_station_single.*

class StationSingleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpListeners()
    }

    private fun setUpListeners() {
//        backButton.setOnClickListener {
//            replaceFragment(this, R.id.homeContainer, HomeMapFragment.newInstance())
//        }
        bookNowButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, BookingPlugsFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = StationSingleFragment()
    }
}