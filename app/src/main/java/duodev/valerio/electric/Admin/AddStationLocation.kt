package duodev.valerio.electric.Admin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.toast
import kotlinx.android.synthetic.main.activity_add_station_location.*

class AddStationLocation : AppCompatActivity(), OnMapReadyCallback {

    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var mLat: Double = 0.0
    private var mLong: Double = 0.0
    private var address = ""
    private lateinit var mMap: GoogleMap
    private lateinit var autocompleteFragment: AutocompleteSupportFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_station_location)
        stationLocation.onCreate(savedInstanceState)

        Log.d("TAG", "onCreate: " + R.string.google_maps_key)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))



        init()
    }

    private fun init() {
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))


        stationLocation.onResume()
        getLocation()
        setUpListeners()
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {

            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                if (it != null) {
                    mLat = it.latitude
                    mLong = it.longitude
                    setUpMap()
                }
            }
        }
    }

    private fun setUpListeners() {
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                moveCamera(place.latLng, 15f)
                Log.i("TAG!!!!!!", "Place: ${place.name}, ${place.id}")
            }

            override fun onError(p0: Status) {
            }
        })

        selectButton.setOnClickListener {
            if (lat != 0.0 && long != 0.0) {
                Log.d("TATATA","sent intent")

                val geocoder = Geocoder(this).getFromLocation(lat, long, 1)
                address = geocoder[0].getAddressLine(0)

                val intent = Intent()
                intent.putExtra(ADDRESS, address)
                intent.putExtra(LAT, lat)
                intent.putExtra(LONG, long)
                setResult(AdminPanelFragment.CODE, intent)
                finish()
                overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
            } else {
                toast("select location")
            }
        }
    }

    private fun setUpMap() {
        try {
            MapsInitializer.initialize(applicationContext)
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.stationLocation) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
    private fun moveCamera(
        latlng: LatLng?,
        zoom: Float
    ) {
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
        mMap.addMarker(MarkerOptions().position(latlng!!))
        lat = latlng.latitude
        long = latlng.longitude


    }
//    override fun onResume() {
//        super.onResume()
//        stationLocation.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stationLocation.onDestroy()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stationLocation.onPause()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        stationLocation.onLowMemory()
//    }

    companion object {

        const val ADDRESS = "address"
        const val LAT = "latitude"
        const val LONG = "longitude"

        fun newInstance(context: Context) = Intent(context, AddStationLocation::class.java)

    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        p0.let {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            it!!.isMyLocationEnabled = true
            val style = MapStyleOptions.loadRawResourceStyle(this, R.raw.night_maps)
            it.setMapStyle(style)

            val latLng = LatLng(mLat, mLong)
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
            it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            it.setOnMapClickListener {latLong ->
                it.clear()
                it.addMarker(MarkerOptions().position(latLong))
                lat = latLong.latitude
                long = latLong.longitude
                Log.d("TATATA", "$lat $long")
            }
        }

    }
}
