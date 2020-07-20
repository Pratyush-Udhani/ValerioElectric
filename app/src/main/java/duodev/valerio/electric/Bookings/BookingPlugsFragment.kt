package duodev.valerio.electric.Bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import duodev.valerio.electric.Bookings.Adapter.BookingPlugAdapter
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.pojos.Ports
import kotlinx.android.synthetic.main.fragment_booking_plugs.*
import java.io.Serializable

class BookingPlugsFragment : Fragment(), BookingPlugAdapter.OnClick {

    private val plugAdapter by lazy { BookingPlugAdapter(mutableListOf(), this) }
    private var selected = false
    private lateinit var plug: Ports
    private lateinit var station: Serializable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            station = it.getSerializable(STATION)!!
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
        setUpObservers()
    }

    private fun setListeners() {
        bookPlugButton.setOnClickListener {
            if (selected) {
                replaceFragment(this, R.id.homeContainer, BookingSlotFragment.newInstance(plug, station))
            } else {
                activity?.toast("Select a plug")
            }
        }
    }

    private fun setUpObservers() {
        val list: MutableList<Ports> = mutableListOf()
        for (i in 0..3) {
            list.add(
                Ports(
                    portName = "CCS$i",
                    portCost = "Rs. 5.56 per hour"
                )
            )
        }
        plugAdapter.addData(list)
    }

    private fun setUpRecycler() {
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

    override fun onItemClicked(ports: Ports) {
        selected = true
        plug = ports
    }
}