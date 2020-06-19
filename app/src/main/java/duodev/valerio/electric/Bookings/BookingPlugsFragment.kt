package duodev.valerio.electric.Bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.Bookings.Adapter.BookingPlugAdapter
import duodev.valerio.electric.R
import duodev.valerio.electric.pojos.Ports
import kotlinx.android.synthetic.main.fragment_booking_plugs.*

class BookingPlugsFragment : Fragment(), BookingPlugAdapter.OnClick {

    private val plugAdapter by lazy { BookingPlugAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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
        setUpRecycler()
        setUpObservers()
    }

    private fun setUpObservers() {
        val list: MutableList<Ports> = mutableListOf()
        for (i in 0..3) {
            list.add(
                Ports(
                    portImageRes = R.drawable.ic_ccs_icon_white,
                    portName = "CCS$i",
                    portCost = "askkjcbajksc"
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
        fun newInstance() = BookingPlugsFragment()
    }
}