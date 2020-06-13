package duodev.valerio.electric.Station

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.Home.HomeMapFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.Station.ViewModel.StationListViewModel
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_station_list.*

class StationListFragment : Fragment(), StationListAdapter.OnClick {

    private val stationAdapter by lazy {
        StationListAdapter(
            mutableListOf(),
            this
        )
    }
    private val stationListViewModel = StationListViewModel()
    private var stationList: MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                replaceFragment(
                    this@StationListFragment,
                    R.id.homeContainer,
                    HomeMapFragment.newInstance())
            }
        })
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        stationListViewModel.fetchData()
        setUpObservers()
        setUpRecycler()
        setUpListeners()
    }

    private fun setUpObservers() {
        stationListViewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                for (element in it){
                    stationList.add(element)
                }
                    stationAdapter.addData(stationList)
            }
        })
    }



    private fun setUpListeners() {
        backButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer , HomeMapFragment.newInstance())
        }
        filterButton.setOnClickListener {
            addFragment(this, R.id.homeContainer, StationFilterFragment.newInstance())
        }
    }

    private fun setUpRecycler() {
        stationListRecycler.apply {
            adapter = this@StationListFragment.stationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    companion object {
        fun newInstance() = StationListFragment()
    }
}