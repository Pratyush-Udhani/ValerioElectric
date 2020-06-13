package duodev.valerio.electric.Station

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationCompanyAdapter
import duodev.valerio.electric.Station.ViewModel.StationCompanyViewModel
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.pojos.Company
import kotlinx.android.synthetic.main.fragment_station_company.*
import kotlinx.android.synthetic.main.fragment_station_filter.*
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlinx.android.synthetic.main.fragment_station_list.backButton

class StationCompanyFragment : Fragment(), StationCompanyAdapter.OnClick {

    private val companyAdapter by lazy { StationCompanyAdapter(mutableListOf(), this) }
    private val stationCompanyViewModel = StationCompanyViewModel()
    private var companyList: MutableList<Company> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                replaceFragment(
                    this@StationCompanyFragment,
                    R.id.homeContainer,
                    StationFilterFragment.newInstance()
                )
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
        return inflater.inflate(R.layout.fragment_station_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        stationCompanyViewModel.fetchData()
        setUpObservers()
        setUpListeners()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        companyRecycler.apply {
            adapter = this@StationCompanyFragment.companyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpObservers() {
        stationCompanyViewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                for (element in it){
                    companyList.add(element)
                }
                companyAdapter.addItems(companyList)
            }
        })
    }

    private fun setUpListeners() {
        backButton.setOnClickListener {
            replaceFragment(this,R.id.homeContainer, StationFilterFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = StationCompanyFragment()
    }
}