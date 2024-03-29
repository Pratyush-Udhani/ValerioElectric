package duodev.valerio.electric.Services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import duodev.valerio.electric.R
import duodev.valerio.electric.Services.Adapter.ServiceListAdapter
import duodev.valerio.electric.Services.ViewModel.ServiceListViewModel
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.makeGone
import duodev.valerio.electric.Utils.makeVisible
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.ServiceStation
import kotlinx.android.synthetic.main.fragment_service_list.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class ServiceListFragment: BaseFragment(), ServiceListAdapter.OnClick  {

    private val serviceListAdapter by lazy { ServiceListAdapter(mutableMapOf<ServiceStation, String>() as LinkedHashMap<ServiceStation, String>, this) }
    private val serviceViewModel = ServiceListViewModel()
    private val sortedList: MutableList<ServiceStation> = mutableListOf()
    private var serviceList: List<ServiceStation> = emptyList()
    private var bookedList: List<ServiceStation> = emptyList()
    private var bookedFetched = MutableLiveData(false)
    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0

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
        getLocation()
        setUpRecycler()
//        setUpObserver()
    }

    private fun setUpObserver() {
            Log.d("TREEE", serviceList.toString())
            bookedFetched.observe(viewLifecycleOwner, Observer {
                if (bookedFetched.value!!) {
                  if (bookedList.isNotEmpty()) {
                      checkStatus(serviceList, bookedList)
                  } else {
                      sortData(serviceList)
                  }
                }
            })
    }

    private fun setUpRecycler() {
        serviceListRecycler.apply {
            adapter = this@ServiceListFragment.serviceListAdapter
            layoutManager = LinearLayoutManager(requireContext())
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
            fetchServices()
        }
    }


    private fun fetchServices() {

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
                    serviceViewModel.fetchData().observe(viewLifecycleOwner, Observer {list ->
                        if (list.isNotEmpty()) {
                            Log.d("TREEE", "service list not empty")
                            serviceList = list
                            setUpObserver()
                        } else {
                            loader.makeGone()
                        }
                    })
                    serviceViewModel.fetchBookings().observe(viewLifecycleOwner, Observer { book ->
                        if (book.isNotEmpty()) {
                            Log.d("TREEE", "booked list not empty")
                            bookedList = book
                            bookedFetched.value = true
//                                checkStatus(serviceList, bookedList)
                        } else {
                            Log.d("TREEE", "service list empty")
                            bookedFetched.value = true
                        }
                    })
                }
            }

//            longitude = location?.longitude!!
//            latitude = location?.latitude
        }

        log("$latitude$longitude latlng")
    }

    private fun checkStatus(list: List<ServiceStation>, bookedList: List<ServiceStation>) {

        Log.d("HEHEHE", "inside fun")

        val stringList: MutableList<String> = mutableListOf()

        for (element in bookedList) {
            if (element.status != "paid") {
                if (!stringList.contains(element.id)) {
                    stringList.add(element.id)
                }
            }
        }

        for (element in list) {
            if (stringList.contains(element.id)) {
                for (elem in bookedList) {
                    if (elem.id == element.id) {
                            sortedList.add(elem)
                            if (sortedList.size == list.size) {
                                sortData(sortedList)
                                break
                            }
                        }
                }
            } else {
                sortedList.add(element)
                if (sortedList.size == list.size) {
                    sortData(sortedList)
                    break
                }
            }
        }
    }


    private fun sortData(list: List<ServiceStation>) {
        //   getLocation()
        val sortedMap: LinkedHashMap<ServiceStation, String> = mutableMapOf<ServiceStation, String>() as LinkedHashMap<ServiceStation, String>
        val sortedList: MutableList<ServiceStation> = list as MutableList<ServiceStation>
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
        for (element in sortedList) {

            sortedMap[element] =
                distance(latitude!!, longitude!!, element.location.latitude, element.location.longitude).toString()
        }
        loader.makeGone()
        serviceListAdapter.addData(sortedMap)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchServices()
                permissionText.makeGone()
            } else {
                requireContext().toast("Please grant permissions")
                permissionText.makeVisible()
                loader.makeGone()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        serviceListAdapter.clearData()
        sortedList.clear()
        bookedFetched.value = false
        serviceList = emptyList()
        bookedList = emptyList()
        init()
    }

    companion object {

        const val NAME = "name"
        const val ADDRESS = "address"
        const val PROVIDER = "provider"
        const val PRICE = "price"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ID = "id"
        const val IMAGE_URL = "imageUrl"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val STATUS = "status"
        const val DESC = "description"
        private const val FLAG = "flag"

        fun newInstance(flag: String = " ") = ServiceListFragment().apply {
            arguments = Bundle().apply {
                putString(FLAG, flag)
            }
        }
    }
    override fun onServiceClicked(serviceStation: ServiceStation, dist: String) {
        val map: HashMap<String, Any> = hashMapOf()

        map[NAME] = serviceStation.serviceName
        map[ADDRESS] = serviceStation.serviceAddress
        map[PRICE] = serviceStation.servicePrice
        map[PROVIDER] = serviceStation.serviceProvider
        map[IMAGE_URL] = serviceStation.serviceImage
        map[LATITUDE] = serviceStation.location.latitude
        map[LONGITUDE] = serviceStation.location.longitude
        map[ID] = serviceStation.id
        map[STATUS] = serviceStation.status
        map[DESC] = serviceStation.description

        startActivity(ServiceSingleActivity.newInstance(requireContext(), map, dist))
        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }
}
