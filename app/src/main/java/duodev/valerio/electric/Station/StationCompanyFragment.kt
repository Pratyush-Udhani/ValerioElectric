package duodev.valerio.electric.Station

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationCompanyAdapter
import duodev.valerio.electric.Station.StationSingleActivity.Companion.FLAG
import duodev.valerio.electric.Station.ViewModel.StationCompanyViewModel
import duodev.valerio.electric.Utils.FILTER
import duodev.valerio.electric.Utils.FILTER_CHARGER_COMPANY
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.pojos.Company
import kotlinx.android.synthetic.main.fragment_station_company.*
import kotlinx.android.synthetic.main.fragment_station_list.backButton

class StationCompanyFragment : Fragment(), StationCompanyAdapter.OnClick {

    private val companyAdapter by lazy { StationCompanyAdapter(mutableListOf(), this) }
    private val stationCompanyViewModel = StationCompanyViewModel()
    private var companyList: MutableList<Company> = mutableListOf()
    private var filterCompanyList: ArrayList<String> = arrayListOf()
    private var filterList: HashMap<String,Any> = hashMapOf()
    private var flag: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(FLAG).toString()
            Log.d("StationCompanyFragment", "onCreate flag: $flag")

            if(flag == FILTER){
                filterList = it.getSerializable("filterList") as HashMap<String, Any>
            }
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
        if ( flag == FILTER){
            filterCompanyList = filterList[FILTER_CHARGER_COMPANY] as ArrayList<String>
        }
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
                companyList.sortBy { company -> company.name}

                companyAdapter.addItems(companyList)
                if(flag == FILTER ){
                    companyAdapter.addChecked(filterCompanyList)
                }
            }
        })
    }

    private fun setUpListeners() {
        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
        applyCompany.setOnClickListener {
//            addFragment(this,R.id.homeContainer, StationFilterFragment.filterCompanyInstance(
//                FILTER_CHARGER_COMPANY,filterCompanyList),null,true)
            addFragment(this,R.id.homeContainer, StationFilterFragment.filteredInstance(
                FILTER,filterList),null,true)
        }
    }

    companion object {
        fun newInstance() = StationCompanyFragment()
        fun filteredInstance(flag: String ="",filterList: HashMap<String,Any>) = StationCompanyFragment().apply {
            arguments = Bundle().apply {
//                this.putStringArrayList("filteredCompanyList",CompanyList)
                this.putString(StationSingleActivity.FLAG,flag)
                this.putSerializable("filterList",filterList)
            }
        }
    }

    override fun onCheckedChanged(company: Company, isChecked: Boolean) {
        Log.d("CompanyFragment", "onCheckedChanged Company: ${company.name.toString()} ")
//        Log.d("CompanyFragment", "onCheckedChanged Company: ${filterCompanyList.toString()} ")
        if(isChecked) filterCompanyList.add(company.name) else filterCompanyList.remove(company.name)
    }
}