package duodev.valerio.electric.Services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.valerio.electric.R
import duodev.valerio.electric.Services.Adapter.ServiceCompanyAdapter
import duodev.valerio.electric.Services.ViewModel.ServiceCompanyViewModel
import duodev.valerio.electric.Station.StationFilterFragment
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Company
import kotlinx.android.synthetic.main.fragment_station_company.*
import kotlinx.android.synthetic.main.fragment_station_filter.*
import kotlinx.android.synthetic.main.fragment_service_list.*
import kotlinx.android.synthetic.main.fragment_service_list.backButton

class ServiceCompanyFragment : BaseFragment(), ServiceCompanyAdapter.OnClick {

    private val companyAdapter by lazy { ServiceCompanyAdapter(mutableListOf(), this) }
    private val serviceCompanyViewModel = ServiceCompanyViewModel()
    private var companyList: MutableList<Company> = mutableListOf()

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
        return inflater.inflate(R.layout.fragment_station_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        serviceCompanyViewModel.fetchData()
        setUpObservers()
        setUpListeners()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        companyRecycler.apply {
            adapter = this@ServiceCompanyFragment.companyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpObservers() {
        serviceCompanyViewModel.data.observe(viewLifecycleOwner, Observer {
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
        fun newInstance() = ServiceCompanyFragment()
    }
}