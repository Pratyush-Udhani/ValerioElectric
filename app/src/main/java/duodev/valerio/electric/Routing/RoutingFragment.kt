package duodev.valerio.electric.Routing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import duodev.valerio.electric.Admin.AddStationLocation
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Routing.Path.FetchURL
import duodev.valerio.electric.Routing.Path.TaskLoadedCallback
import duodev.valerio.electric.Station.Adapter.StationListAdapter
import duodev.valerio.electric.Station.Repo.StationListRepo
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Station.StationSingleActivity
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.databinding.FragmentRoutingBinding
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlin.math.abs

class RoutingFragment : BaseFragment(),TaskLoadedCallback, StationListAdapter.OnClick {
    private val stationAdapter by lazy { StationListAdapter(mutableMapOf<Station, String>() as LinkedHashMap<Station, String>, this) }
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var isStartingPointBtnPressed: Boolean = false;
    private var isDestinationPointBtnPressed: Boolean = false;
    private lateinit var binding: FragmentRoutingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_routing,container,false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
        getLocation()
        setUpRecycler()
    }
    private fun setUpRecycler() {
        binding.RoutingRecyclerView.apply {
            adapter = this@RoutingFragment.stationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setListeners() {

        binding.startingButton.setOnClickListener {
            isStartingPointBtnPressed = true;
            startActivityForResult(
                AddStationLocation.newInstance(requireContext()),
                AdminPanelFragment.CODE
            )
            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        binding.destinationButton.setOnClickListener {
            isDestinationPointBtnPressed = true;
            startActivityForResult(
                AddStationLocation.newInstance(requireContext()),
                AdminPanelFragment.CODE
            )
            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        binding.createRouteButton.setOnClickListener {
//            val uri =
//                Uri.parse("https://www.google.com/maps/dir/?api=1&origin=${binding.startingPoint.text}&destination=${binding.destinationPoint.text}&waypoints=Jodhpur")
//            val i = Intent(Intent.ACTION_VIEW, uri)
//            i.setPackage("com.google.android.apps.maps")
//            startActivity(i)
            FetchURL(this).execute("https://maps.googleapis.com/maps/api/directions/json?origin=${binding.startingPoint.text}&destination=${binding.destinationPoint.text}&key=AIzaSyCE3OiFE3yTHdGMU1unJ2tXd2Gv7Em4Jbg")

        }

        binding.backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
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
                    lat = it.latitude
                    long = it.longitude
                }
            }
        }
    }

    private fun findStations(points: ArrayList<LatLng?>): ArrayList<Station> {
        if(Station_List.isNotEmpty()) {
            Log.d(TAG, "findStations: ${Station_List.size}")
            val stationInRoute: ArrayList<Station> = ArrayList()
            var i = 0
            while (i < points.size) {
                if(i < points.size - 1) {
                    val k = i
                    while ( (i < points.size - 1) && distance(
                            points[i + 1]!!.latitude,points[i + 1]!!.longitude, points[k]!!.latitude, points[k]!!.longitude
                        ) < 1
                    ) {
//                        Log.d(TAG, "findStations: distance:${distance(
//                            points[i + 1]!!.latitude,points[i + 1]!!.longitude, points[i]!!.latitude, points[i]!!.longitude)}")
                        i += 1
                    }
                    for (j in 0 until Station_List.size) {

                        val lat = Station_List[j].location.latitude
                        val long = Station_List[j].location.longitude
                        if (abs(distance(lat, long, points[i]!!.latitude, points[i]!!.longitude)) <= 1) {
                            stationInRoute.add(Station_List[j])
                        }
                    }
                }
                i += 1
            }
            return stationInRoute
        }else{
            return ArrayList<Station>()
        }
    }

    private fun sortData(list: List<Station>) {
        //   getLocation()
        val sortedMap: LinkedHashMap<Station, String> = mutableMapOf<Station, String>() as LinkedHashMap<Station, String>
        val sortedList: MutableList<Station> = list as MutableList<Station>
        for (i in list.indices) {
            for (j in 0 until list.size - i -1) {
                val distanceOne = distance(lat!!, long!!, sortedList[j].location.latitude, sortedList[j].location.longitude)
                val distanceTwo = distance(lat!!, long!!, sortedList[j+1].location.latitude, sortedList[j+1].location.longitude)
                if (distanceOne > distanceTwo) {
                    val temp = sortedList[j]
                    sortedList[j] = sortedList[j+1]
                    sortedList[j+1] = temp
                }
            }
        }
        for (element in sortedList) {

            sortedMap[element] =
                distance(lat!!, long!!, element.location.latitude, element.location.longitude).toString()
        }
        stationAdapter.addData(sortedMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AdminPanelFragment.CODE) {
            Log.d("TATATA", "received intent")
            if (data != null) {
                Log.d("TATATA", "data not null")
                lat = data.getDoubleExtra(AddStationLocation.LAT, 0.0)
                long = data.getDoubleExtra(AddStationLocation.LONG, 0.0)

                if (isStartingPointBtnPressed) {
                    binding.startingPoint.setText(data.getStringExtra(AddStationLocation.ADDRESS))
                    isStartingPointBtnPressed = false;
                }
                if (isDestinationPointBtnPressed) {
                    binding.destinationPoint.setText(data.getStringExtra(AddStationLocation.ADDRESS))
                    isDestinationPointBtnPressed = false;
                }
                Log.d("TATATATA", "$lat, $long")
            }
        }
    }

    companion object {
        fun newInstance() = RoutingFragment()
        const val TAG = "Routing"
    }

    override fun onTaskDone(list: java.util.ArrayList<LatLng?>) {
        val s = findStations(list)
        sortData(s)
        Log.d(TAG, "onTaskDone: Points:${list.size.toString()}")
        for(i in s) {
            Log.d(TAG, "onTaskDone: Station:${i.stationAddress}")
        }
        binding.textViewPoints.text = "Stations: ${s.size.toString()}"

    }

    override fun onStationClicked(station: Station, dist: String) {
        Log.d("CLICKED", "clicked")

        val map: HashMap<String, Any> = hashMapOf()
        map[StationListFragment.OWNER] = station.ownerCompany
        map[StationListFragment.CONNECTOR] = station.connectorType
        map[StationListFragment.ADDRESS] = station.stationAddress
        map[StationListFragment.PROVIDER] = station.serviceProvider
        map[StationListFragment.LATITUDE] = station.location.latitude
        map[StationListFragment.LONGITUDE] = station.location.longitude
        map[StationListFragment.LOCATION] = station.stationLocation
        map[StationListFragment.ID] = station.stationId
        map[StationListFragment.IMAGE_URL] = station.imageUrl
        map[StationListFragment.OWNERSHIP] = station.ownership
//        map[StationListFragment.SLOTS] = station.numberOfStations
        log("called")
        startActivityForResult(
            StationSingleActivity.newInstance(requireContext(), map, dist),
            StationListFragment.RESULT_CODE
        )
//        startActivity(StationSingleActivity.newInstance(requireContext(), map, dist))
        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }

    override fun onStationLongClicked(station: Station) {

    }
}