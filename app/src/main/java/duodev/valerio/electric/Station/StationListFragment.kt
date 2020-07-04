package duodev.valerio.electric.Station

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Home.HomeMapFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.Station.ViewModel.StationListViewModel
import duodev.valerio.electric.Utils.StationDef
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class StationListFragment : Fragment(), StationListAdapter.OnClick {

    private val stationAdapter by lazy {
        StationListAdapter(
            mutableListOf(),
            this
        )
    }
    private val stationListViewModel = StationListViewModel()
    private var stationList: MutableList<Station> = mutableListOf()
    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0


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
        return inflater.inflate(R.layout.fragment_station_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        getLocation()
        setUpObservers()
        setUpRecycler()
        setUpListeners()
    }

    private fun setUpObservers() {
        stationListViewModel.fetchData().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                sortData(it)
            }
        })
    }

    private fun sortData(list: List<Station>) {
        val sortedList: MutableList<Station> = list as MutableList<Station>
        for (i in list.indices) {
            for (j in 0 until list.size - i -1) {
                val distanceOne = distance(latitude!!, longitude!!, sortedList[j].location.latitude, sortedList[j].location.longitude)
                val distanceTwo = distance(latitude!!, longitude!!, sortedList[j+1].location.latitude, sortedList[j+1].location.longitude)
                if (distanceOne > distanceTwo) {
                    val temp = sortedList[j]
                    sortedList[j] = sortedList[j+1]
                    sortedList[j+1] = temp
                }
            }
        }
        stationAdapter.addData(sortedList)
    }

    private fun setUpListeners() {
//        backButton.setOnClickListener {
//            replaceFragment(this, R.id.homeContainer , HomeMapFragment.newInstance())
//        }
        filterButton.setOnClickListener {
            setUpDB()
        }
    }

    private fun setUpDB() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val list = StationDef.getStationList()

        for (element in list) {
            Log.d("LISTS", " ${element.stationId}  ${element.toString()}")
            firestore.collection("Stations").document(element.stationId).set(element)
        }
    }

    private fun setUpRecycler() {
        stationListRecycler.apply {
            adapter = this@StationListFragment.stationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun getLocation() {
        val lm =
            (activity as HomeActivity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            getLocation()
            return
        }
        val location =  lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        longitude = location?.longitude
        latitude = location?.latitude
        stationListViewModel.fetchData()
    }

    companion object {

        const val ADDRESS = "address"
        const val NAME = "name"
        const val PROVIDER = "provider"
        const val CONNECTOR = "connector"
        const val OWNER = "owner"
        const val LATITUDE = "Latitude"
        const val LONGITUDE = "Longitude"
        const val ID = "id"
        const val LOCATION = "Location"
        const val IMAGE_URL = "ImageUrl"
        const val RESULT_CODE = 12

        fun newInstance() = StationListFragment()
    }

    override fun onStationClicked(station: Station) {
        val map: HashMap<String, Any> = hashMapOf()
        map[OWNER] = station.ownerCompany
     //   map[CONNECTOR] = station.connectorType
        map[NAME] = station.stationName
        map[ADDRESS] = station.stationAddress
        map[PROVIDER] = station.serviceProvider
        map[LATITUDE] = station.location.latitude
        map[LONGITUDE] = station.location.longitude
        map[ID] = station.stationId
        map[IMAGE_URL] = station.imageUrl
        log("called")
        startActivity(StationSingleActivity.newInstance(requireContext(), map))
        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }
}