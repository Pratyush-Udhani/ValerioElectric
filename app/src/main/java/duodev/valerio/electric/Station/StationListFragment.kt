package duodev.valerio.electric.Station

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.Bookings.BookingPlugsFragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.Station.ViewModel.StationListViewModel
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseActivity
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class StationListFragment : Fragment(), StationListAdapter.OnClick {

    private val stationAdapter by lazy { StationListAdapter(mutableMapOf<Station, String>() as LinkedHashMap<Station, String>, this) }
    private val stationListViewModel = StationListViewModel()
    private var stationList: MutableList<Station> = mutableListOf()
    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0
    private var flag = ""
    private val dialog by lazy { Dialog(requireContext()) }
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var isDeleted = false;
    private var filterList: HashMap<String,Any> = hashMapOf()
    private var isFilter: HashMap<String,Boolean> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(FLAG).toString()
            Log.d("StationListFragment", "onCreate flag: $flag")
            if(flag == FILTER){
                filterList = (it.getSerializable("filterList") as HashMap<String,Any> )
            }
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
        if(flag == FILTER)
            setUpFilterList()
        setUpRecycler()
        getLocation()
        setUpListeners()
    }

    private fun setUpFilterList(){
        Log.d("FilterList", "setUpFilterList ")
        isFilter[FILTER_CHARGER_COMPANY] = (filterList[FILTER_CHARGER_COMPANY] as ArrayList<String>).isNotEmpty()
        isFilter[FILTER_PORT_TYPE] = (filterList[FILTER_PORT_TYPE] as MutableList<String>).isNotEmpty()
        isFilter[FILTER_RADIUS] = (filterList[FILTER_RADIUS] as Int) > 0
        isFilter[FILTER_CHARGER_SPEED] = (filterList[FILTER_CHARGER_SPEED] as MutableList<String>).isNotEmpty()
    }

    private fun setUpdb() {

        val list = StationDef.getStationList()
        val serviceList = ServiceDef.getServiceList()

        serviceList.forEachIndexed { index, serviceStation ->
            firestore.collection(SERVICES).document("$index").set(serviceStation)
        }

            list.forEachIndexed { index, station ->
                firestore.collection(STATIONS).document("$index").set(station)
            }
    }

    private fun filterSpeed(station: Station): Boolean {
        if (station.connectorType.isNotEmpty()) {
            for (i in station.connectorType) {
                if ((filterList[FILTER_CHARGER_SPEED] as MutableList<String>).contains(i.speed.toUpperCase())) {
                    return true
                }
            }
        }
        return false
    }
    private fun filterPortType(station: Station): Boolean {
        if (station.connectorType.isNotEmpty()) {
            for (i in station.connectorType) {
                if ((filterList[FILTER_PORT_TYPE] as MutableList<String>).contains(i.type)) {
                    return true
                }
            }
        }
        return false
    }
    private fun sortData(list: List<Station>) {
        //   getLocation()
        val sortedMap: LinkedHashMap<Station, String> = mutableMapOf<Station, String>() as LinkedHashMap<Station, String>
        var sortedList: MutableList<Station> = list as MutableList<Station>

        if(flag == FILTER) {
            if (isFilter[FILTER_CHARGER_COMPANY]!!) {
                Log.d(FILTER, "sortData: FILTER CHARGER COMPANY LIST "+(filterList[FILTER_CHARGER_COMPANY] as MutableList<String>).toString())

                sortedList = sortedList.filter {
                    (filterList[FILTER_CHARGER_COMPANY] as MutableList<String>).contains(it.ownerCompany.name)
                } as MutableList<Station>

            }
            Log.d(FILTER, "sortData: FILTER CHARGER COMPANY "+filterList[FILTER_CHARGER_COMPANY].toString())
            if (isFilter[FILTER_CHARGER_SPEED]!!) {
                sortedList = sortedList.filter {
                    filterSpeed(it)
                } as MutableList<Station>
            }
            Log.d(FILTER, "sortData: FILTER CHARGER SPEED "+filterList[FILTER_CHARGER_SPEED].toString())

            if (isFilter[FILTER_PORT_TYPE]!!) {
                sortedList = sortedList.filter {
                    filterPortType(it)
                } as MutableList<Station>
            }
            Log.d(FILTER, "sortData: FILTER PORT TYPE "+filterList[FILTER_PORT_TYPE].toString())

        }
        if(sortedList.isNotEmpty()) {
            for (i in sortedList.indices) {
                for (j in 0 until sortedList.size - i - 1) {
                    val distanceOne = distance(
                        latitude!!,
                        longitude!!,
                        sortedList[j].location.latitude,
                        sortedList[j].location.longitude
                    )
                    val distanceTwo = distance(
                        latitude!!,
                        longitude!!,
                        sortedList[j + 1].location.latitude,
                        sortedList[j + 1].location.longitude
                    )

                    if (distanceOne > distanceTwo) {
                        val temp = sortedList[j]
                        sortedList[j] = sortedList[j + 1]
                        sortedList[j + 1] = temp
                    }

                }
            }

            for (element in sortedList) {
                val dist =  distance(
                    latitude!!,
                    longitude!!,
                    element.location.latitude,
                    element.location.longitude
                )
                if(flag == FILTER ) {
                    if(isFilter[FILTER_RADIUS]!!) {
                        Log.d("FILTER", "sortData: filterList[FILTER_RADIUS] "+filterList[FILTER_RADIUS])

                        if (miles2km(dist) <= (filterList[FILTER_RADIUS] as Int)) {
                            sortedMap[element] = dist.toString()
                            Log.d("FILTER", "sortedMap[element]: dist "+ dist)
                        }
                    }
                    else
                        sortedMap[element] = dist.toString()

                }else {
                    sortedMap[element] = dist.toString()
                }
            }
            loader.makeGone()
            stationAdapter.addData(sortedMap)
        }
        loader.makeGone()


    }

    private fun setUpListeners() {
//        backButton.setOnClickListener {
//            replaceFragment(this, R.id.homeContainer , HomeMapFragment.newInstance())
//        }
        filterButton.setOnClickListener {
            if( flag == FILTER)
                addFragment(this,R.id.homeContainer, StationFilterFragment.filteredInstance(FILTER,filterList))
            else
                addFragment(this, R.id.homeContainer, StationFilterFragment.newInstance(), null, true)
//            setUpdb()
        }

        permissionText.setOnClickListener {
            Log.d("CLICKEDS", "clicked")
            getLocation()
        }
    }

    private fun setUpRecycler() {
        stationListRecycler.apply {
            adapter = this@StationListFragment.stationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CODE) {
            replaceFragment(this, R.id.homeContainer, BookingPlugsFragment.newInstance(data?.getSerializableExtra(StationSingleActivity.STATION)))
        }
    }

    private fun changeFragment(fragment: Fragment, tag: Boolean) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.homeContainer, fragment)
        if (tag)
            fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE), 1)
        } else {
            fetchStations()
        }

    }

    private fun fetchStations() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
//            val location =  lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
                log("success")
                if (it != null) {
                    log("not null")
                    latitude = it.latitude
                    longitude = it.longitude
                    stationListViewModel.fetchData().observe(viewLifecycleOwner, Observer {list ->
                        if (list.isNotEmpty()) {
                            log("called$list")
                            Station_List = list as MutableList<Station>
                            sortData(list)
                        } else {
                            loader.makeGone()
                            noStationText.makeVisible()
                        }
                    })
                }
            }

//            longitude = location?.longitude!!
//            latitude = location?.latitude
        }

        log("$latitude$longitude latlng")
    }

    private fun showDialog(station: HashMap<String, Any>,stationToRemove: Station){
        val view: View = layoutInflater.inflate(R.layout.dialog_station_menu, null)

        dialog.apply {
            setContentView(view)
            create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val editButton = findViewById<Button>(R.id.editButton)
            val deleteButton = findViewById<Button>(R.id.deleteButton)
            deleteButton.setOnClickListener{
                firestore.collection("Stations")
                    .document(station[ID].toString())
                    .delete()
                    .addOnSuccessListener {
                        activity?.toast("Deletion Successful")
                        isDeleted = true;
                        stationAdapter.removeStation(stationToRemove)
                        dismiss()
                        Log.d("StationListFragment", "showDialog: Deletion Successful")
                    }
                    .addOnFailureListener{
                        activity?.toast("Deletion Failed")
                        Log.d("StationListFragment", "showDialog: Deletion failed ")
                    }
            }
            editButton.setOnClickListener {
                replaceFragment(this@StationListFragment,R.id.homeContainer,AdminPanelFragment.editInstance(EDIT,station),null)
                dismiss()
            }
            show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchStations()
                permissionText.makeGone()
            } else {
                requireContext().toast("Please grant permissions")
                permissionText.makeVisible()
                loader.makeGone()
            }
        }
    }


    companion object {

        const val ADDRESS = "address"
        const val PROVIDER = "provider"
        const val CONNECTOR = "connector"
        const val OWNER = "owner"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ID = "id"
        const val LOCATION = "location"
        const val IMAGE_URL = "imageUrl"
        const val OWNERSHIP = "ownership"
        const val SLOTS = "slots"
        const val RESULT_CODE = 12
        private const val FLAG = "flag"

        fun newInstance(flag: String = "") = StationListFragment().apply {
            arguments = Bundle().apply {
                putString(FLAG, flag)
            }
        }

        fun filteredInstance(flag: String = "",filterList: HashMap<String,Any>) = StationListFragment().apply {
            arguments = Bundle().apply {
                putString(FLAG,flag)
                putSerializable("filterList",filterList)
            }
        }

    }

    override fun onStationClicked(station: Station, dist: String) {
        Log.d("CLICKED", "clicked")

        val map: HashMap<String, Any> = hashMapOf()
        map[OWNER] = station.ownerCompany
        map[CONNECTOR] = station.connectorType
        map[ADDRESS] = station.stationAddress
        map[PROVIDER] = station.serviceProvider
        map[LATITUDE] = station.location.latitude
        map[LONGITUDE] = station.location.longitude
        map[LOCATION] = station.stationLocation
        map[ID] = station.stationId
        map[IMAGE_URL] = station.imageUrl
        map[OWNERSHIP] = station.ownership
//        map[SLOTS] = station.numberOfStations
        log("called")
        startActivityForResult(
            StationSingleActivity.newInstance(requireContext(), map, dist),
            RESULT_CODE
        )
//        startActivity(StationSingleActivity.newInstance(requireContext(), map, dist))
        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }

    override fun onStationLongClicked(station: Station) {
        val map: HashMap<String, Any> = hashMapOf()
        map[OWNER] = station.ownerCompany
        map[CONNECTOR] = station.connectorType
        map[ADDRESS] = station.stationAddress
        map[PROVIDER] = station.serviceProvider
        map[LATITUDE] = station.location.latitude
        map[LONGITUDE] = station.location.longitude
        map[LOCATION] = station.stationLocation
        map[ID] = station.stationId
        map[IMAGE_URL] = station.imageUrl
        map[OWNERSHIP] = station.ownership
//        map[SLOTS] = station.numberOfStations

        Log.d("onStationLongClicked","LongClicked")
        Log.d("onStationLongClicked","Station ${station.stationId}" )
        Log.d("onStationLongClicked", "Flag ${pm.email} ")
        if(flag == ADMIN || pm.email == ADMIN_EMAIL ) {
            showDialog(map,station)
        }
    }
}