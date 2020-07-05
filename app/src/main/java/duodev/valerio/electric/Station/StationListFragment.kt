package duodev.valerio.electric.Station

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Bookings.BookingPlugsFragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.Station.ViewModel.StationListViewModel
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class StationListFragment : Fragment(), StationListAdapter.OnClick {

    private val stationAdapter by lazy { StationListAdapter(mutableListOf(), this) }
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
        setUpRecycler()
        getLocation()
        setUpListeners()
    }

    private fun setUpdb() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        val list = StationDef.getStationList()

            list.forEachIndexed { index, station ->
                firestore.collection(STATIONS).document("$index").set(station)
            }
    }

//    private fun setUpObservers() {
//        stationListViewModel.fetchData().observe(viewLifecycleOwner, Observer {
//            if (it.isNotEmpty()) {
//                sortData(it)
//            }
//        })
//    }

    private fun sortData(list: List<Station>) {
        //   getLocation()
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
        loader.makeGone()
        stationAdapter.addData(sortedList)
    }

    private fun setUpListeners() {
//        backButton.setOnClickListener {
//            replaceFragment(this, R.id.homeContainer , HomeMapFragment.newInstance())
//        }
        filterButton.setOnClickListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CODE) {
            changeFragment(BookingPlugsFragment.newInstance(), true)
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
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE), 1)
        } else {
            fetchStations()
        }

    }

    private fun fetchStations() {
        val lm =
            (activity as HomeActivity).getSystemService(Context.LOCATION_SERVICE) as LocationManager

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
                if (it != null) {
                    latitude = it.latitude
                    longitude = it.longitude
                    stationListViewModel.fetchData().observe(viewLifecycleOwner, Observer {list ->
                        if (list.isNotEmpty()) {
                            log("called$list")
                            sortData(list)
                        }
                    })
                }
            }

//            longitude = location?.longitude!!
//            latitude = location?.latitude
        }

        log("$latitude$longitude latlng")
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
            }
        }
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
        Log.d("CLICKED", "clicked")

        val map: HashMap<String, Any> = hashMapOf()
        map[OWNER] = station.ownerCompany
        //   map[CONNECTOR] = station.connectorType
        map[ADDRESS] = station.stationAddress
        map[PROVIDER] = station.serviceProvider
        map[LATITUDE] = station.location.latitude
        map[LONGITUDE] = station.location.longitude
        map[LOCATION] = station.stationLocation
        map[ID] = station.stationId
        map[IMAGE_URL] = station.imageUrl
        log("called")
        startActivityForResult(
            StationSingleActivity.newInstance(requireContext(), map),
            RESULT_CODE
        )
        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }
}