package duodev.valerio.electric.Services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.R
import duodev.valerio.electric.Services.Adapter.ServiceListAdapter
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_service_list.*
import kotlinx.android.synthetic.main.fragment_station_list.*

class ServiceListFragment: Fragment(), ServiceListAdapter.OnClick  {

    private val serviceListAdapter by lazy { ServiceListAdapter(mutableMapOf<Station, String>() as LinkedHashMap<Station, String>, this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init() {
        setUpRecycler()
    }

    private fun setUpRecycler() {
        serviceListRecycler.apply {
            adapter = this@ServiceListFragment.serviceListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onServiceClicked(station: Station, dist: String) {

    }
}