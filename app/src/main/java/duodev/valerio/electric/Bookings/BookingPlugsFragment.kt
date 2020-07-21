package duodev.valerio.electric.Bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import duodev.valerio.electric.Bookings.Adapter.BookingPlugAdapter
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Connector
import kotlinx.android.synthetic.main.fragment_booking_plugs.*
import java.io.Serializable

class BookingPlugsFragment : BaseFragment(), BookingPlugAdapter.OnClick {

    private val plugAdapter by lazy { BookingPlugAdapter(mutableListOf(), this) }
    private var selected = false
    private lateinit var plug: Connector
    private lateinit var station: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            station = it.getSerializable(STATION) as HashMap<String, Any>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_plugs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
        setUpRecycler()
    }

    private fun setListeners() {
        bookPlugButton.setOnClickListener {
            if (selected) {
                addFragment(this, R.id.homeContainer, BookingSlotFragment.newInstance(plug, station))
            } else {
                activity?.toast("Select a plug")
            }
        }

        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun setUpRecycler() {
        plugAdapter.addData(station[StationListFragment.CONNECTOR] as List<Connector>)

        plugRecycler.apply {
            adapter = this@BookingPlugsFragment.plugAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    companion object {

        private const val STATION = "Station"

        fun newInstance(station: Serializable?) = BookingPlugsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(STATION, station)
            }
        }
    }

    override fun onItemClicked(ports: Connector) {
        selected = true
        plug = ports
    }
}