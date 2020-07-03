package duodev.valerio.electric.Station

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.activity_station_single.*

class StationSingleActivity : AppCompatActivity() {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_single)
        init()
    }

    private fun init() {
        mapView.onResume()
        setUpMap()
        setUpListeners()
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

            // lat lang for location

//            val latLng = LatLng(gym[NewHomeFragment.LATITUDE] as Double,gym[NewHomeFragment.LONGITUDE] as Double)
//            googleMap.addMarker(MarkerOptions().position(latLng))
//            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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
        backButton.setOnClickListener {
            replaceFragment(null, R.id.homeContainer, HomeMapFragment.newInstance(), this)
        }
        bookNowButton.setOnClickListener {
            replaceFragment(null, R.id.homeContainer, BookingPlugsFragment.newInstance(), this)
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, StationSingleActivity::class.java)
    }
}