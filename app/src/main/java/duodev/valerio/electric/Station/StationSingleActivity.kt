package duodev.valerio.electric.Station

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import duodev.valerio.electric.Bookings.BookingPlugsFragment
import duodev.valerio.electric.Home.HomeMapFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.activity_station_single.*

class StationSingleActivity : AppCompatActivity() {

    private lateinit var googleMap: GoogleMap
    private lateinit var station: HashMap<String, Any>
    private lateinit var distance: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_single)
        mapView.onCreate(savedInstanceState)
        intent?.let {
            station = it.getSerializableExtra(FLAG) as HashMap<String, Any>
            distance = it.getStringExtra(DIST)!!
        }
        init()
    }

    private fun init() {
        setUpUI()
        mapView.onResume()
        setUpMap()
        setUpListeners()
    }

    private fun setUpUI() {
        stationName.text = station[StationListFragment.ADDRESS].toString()
        stationAddress.text = station[StationListFragment.LOCATION].toString()
        distanceLabel.text = "$distance km"

        val list = station[StationListFragment.CONNECTOR] as List<Connector>

        for (connector in list) {
            when (connector.type) {
                TYPE_ONE -> typeOneLayoutOut.makeVisible()
                TYPE_TWO -> typeTwoLayoutOut.makeVisible()
                CHAD -> chadLayoutOut.makeVisible()
                CCS -> ccsLayoutOut.makeVisible()
            }
        }
    }


    private fun setUpMap() {
        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync {
            googleMap = it
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
            val style = MapStyleOptions.loadRawResourceStyle(this, R.raw.night_maps)
            googleMap.isMyLocationEnabled = true
            googleMap.setMapStyle(style)
            val latLng = LatLng(station[StationListFragment.LATITUDE] as Double,station[StationListFragment.LONGITUDE] as Double)
            googleMap.addMarker(MarkerOptions().position(latLng))
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    private fun setUpListeners() {
//        backButton.setOnClickListener {
//            replaceFragment(null, R.id.homeContainer, HomeMapFragment.newInstance(), this)
//        }
        bookNowButton.setOnClickListener {
//            replaceFragment(null, R.id.homeContainer, BookingPlugsFragment.newInstance(), this)
            val intent = Intent()
            setResult(StationListFragment.RESULT_CODE, intent)
            finish()
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }
    }

    companion object {

        const val FLAG = "flag"
        const val DIST = "dist"

        fun newInstance(context: Context, map: HashMap<String, Any>, dist: String) = Intent(context, StationSingleActivity::class.java).apply {
            putExtra(FLAG, map)
            putExtra(DIST, dist)
        }
    }
}