package duodev.valerio.electric.Station

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationSingleActivity.Companion.FLAG
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.databinding.FragmentStationFilterBinding
import kotlinx.android.synthetic.main.filter_layout_charger_speed.view.*
import kotlinx.android.synthetic.main.filter_layout_port_type.view.*
import kotlinx.android.synthetic.main.filter_layout_radius.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StationFilterFragment : Fragment() {
    private var filterList: HashMap<String,Any> = hashMapOf()
    private var chargerSpeed: MutableList<String> = mutableListOf()
    private var portTypeList: MutableList<String> = mutableListOf()
    private var filterCompanyList: ArrayList<String> = arrayListOf()
    private var radius: Int = 0
    private var flag: String = ""
    private lateinit var binding: FragmentStationFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(FLAG).toString()
            Log.d("StationFilterFragment", "onCreate flag: $flag")

            if(flag == FILTER_CHARGER_COMPANY)
                filterCompanyList = it.getStringArrayList("filterCompanyList") as ArrayList<String>

            if(flag == FILTER) {
                filterList = it.getSerializable("filterList") as HashMap<String, Any>
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_station_filter,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if ( flag == FILTER )
            setUpFilters()
        setUpListeners()
    }

    private fun setUpFilters() {
        Log.d("FILTER", "setUpFiltersUI: "+ filterList.toString())
        chargerSpeed = filterList[FILTER_CHARGER_SPEED] as MutableList<String>
        portTypeList = filterList[FILTER_PORT_TYPE] as MutableList<String>
        filterCompanyList = filterList[FILTER_CHARGER_COMPANY] as ArrayList<String>
        for (i in filterList[FILTER_CHARGER_SPEED] as MutableList<String>) {
            when (i.toUpperCase()){
                CHARGER_SPEED_SLOW.toUpperCase() -> binding.filterChargerSpeed.slowChargerSpeedSwitch.isChecked = true
                CHARGER_SPEED_MODERATE.toUpperCase() -> binding.filterChargerSpeed.moderateChargerSpeedSwitch.isChecked = true
                CHARGER_SPEED_FAST.toUpperCase() -> binding.filterChargerSpeed.fastChargerSpeedSwitch.isChecked = true
            }
        }
        for ( i in filterList[FILTER_PORT_TYPE] as MutableList<String>) {
            when(i){
                CHAD -> binding.filterPortType.chadPortSwitch.isChecked = true
                TYPE_ONE -> binding.filterPortType.typeOnePortSwitch.isChecked = true
                TYPE_TWO -> binding.filterPortType.typeTwoPortSwitch.isChecked = true
                CCS -> binding.filterPortType.ccsPortSwitch.isChecked = true
            }
        }

        binding.filterRadius.radiusSeekBar.progress = filterList[FILTER_RADIUS] as Int
        binding.filterRadius.radiusText.text = filterList[FILTER_RADIUS].toString() + " km"


    }

    private fun setUpListeners() {
        binding.backButton.setOnClickListener {
            replaceFragment(
                this,
                R.id.homeContainer,
                StationListFragment.newInstance()
            )
        }
        //FILTER BUTTON
        binding.applyFiltersButton.setOnClickListener {
            filterList[FILTER_CHARGER_COMPANY] = filterCompanyList
            filterList[FILTER_CHARGER_SPEED] = chargerSpeed
//        FilterList[FILTER_COST] = 0
            filterList[FILTER_RADIUS] = radius
            filterList[FILTER_PORT_TYPE] = portTypeList

            replaceFragment(
                this,
                R.id.homeContainer,
                StationListFragment.filteredInstance(FILTER,filterList)
            )
        }
        //FILTER CHARGER  SPEED
        binding.filterChargerSpeed.slowChargerSpeedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)chargerSpeed.add(CHARGER_SPEED_SLOW.toUpperCase()) else chargerSpeed.remove(CHARGER_SPEED_SLOW.toUpperCase())
        }
        binding.filterChargerSpeed.moderateChargerSpeedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) chargerSpeed.add(CHARGER_SPEED_MODERATE.toUpperCase()) else chargerSpeed.remove(CHARGER_SPEED_MODERATE.toUpperCase())
        }
        binding.filterChargerSpeed.fastChargerSpeedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) chargerSpeed.add(CHARGER_SPEED_FAST.toUpperCase()) else chargerSpeed.remove(CHARGER_SPEED_FAST.toUpperCase())
        }


        //FILTER PORT TYPE
        binding.filterPortType.typeOnePortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) portTypeList.add(TYPE_ONE) else portTypeList.remove(TYPE_ONE)
        }
        binding.filterPortType.typeTwoPortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) portTypeList.add(TYPE_TWO) else portTypeList.remove(TYPE_TWO)
        }

        binding.filterPortType.chadPortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) portTypeList.add(CHAD) else portTypeList.remove(CHAD)
        }
        binding.filterPortType.ccsPortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) portTypeList.add(CCS) else portTypeList.remove(CCS)
        }

        //FILTER RADIUS
        binding.filterRadius.radiusSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.filterRadius.radiusText.text = "$progress km"
                radius = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        // CHOOSE COMPANY BUTTON
        binding.companyList.setOnClickListener {
            filterList[FILTER_CHARGER_COMPANY] = filterCompanyList
            filterList[FILTER_CHARGER_SPEED] = chargerSpeed

            filterList[FILTER_RADIUS] = radius
            filterList[FILTER_PORT_TYPE] = portTypeList
            addFragment(this,R.id.homeContainer,StationCompanyFragment.filteredInstance(FILTER,filterList))
        }
    }

    companion object {
        fun newInstance(flag: String = "") = StationFilterFragment()

        fun filterCompanyInstance(flag: String ="",filterCompanyList: ArrayList<String>) = StationFilterFragment().apply {
            arguments = Bundle().apply {
                this.putStringArrayList("filterCompanyList",filterCompanyList)
                this.putString(FLAG,flag)
            }
        }
        fun filteredInstance(flag: String ="",FilterList: HashMap<String,Any>) = StationFilterFragment().apply {
            arguments = Bundle().apply {
                this.putSerializable("filterList",FilterList)
                this.putString(FLAG,flag)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 123 && resultCode == Activity.RESULT_OK) && data != null) {
            filterCompanyList = data.getStringArrayListExtra("filterCompanyList") ?: ArrayList<String>()
        }
    }


}