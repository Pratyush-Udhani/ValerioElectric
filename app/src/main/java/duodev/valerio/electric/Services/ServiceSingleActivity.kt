package duodev.valerio.electric.Services

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import duodev.valerio.electric.Payment.PaymentActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.base.BaseActivity
import kotlinx.android.synthetic.main.activity_service_single.*

class ServiceSingleActivity : BaseActivity() {

    private lateinit var googleMap: GoogleMap
    private lateinit var service: HashMap<String, Any>
    private lateinit var distance: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_single)
        mapView.onCreate(savedInstanceState)

        intent?.let {
            service = it.getSerializableExtra(FLAG) as HashMap<String, Any>
            distance = it.getStringExtra(DIST)!!
        }
        init()
    }

    private fun init() {
        setUpMap()
        setUpUI()
        mapView.onResume()
        setListeners()
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
            val latLng = LatLng(service[ServiceListFragment.LATITUDE] as Double,service[ServiceListFragment.LONGITUDE] as Double)
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

    private fun setUpUI() {
        serviceName.text = service[ServiceListFragment.NAME].toString()
        serviceAddress.text = service[ServiceListFragment.ADDRESS].toString()
        distanceLabel.text = "$distance km"
        servicePrice.text = "Rs. ${service[ServiceListFragment.PRICE]}"
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }

    private fun setListeners() {

        bookNowButton.setOnClickListener {
            // Add instance of service in PaymentActivity with args as below
            startActivity(PaymentActivity.newInstance(this, service, BOOKING_FLAG))
            // startActivity(PaymentActivity.newInstance(this, service as hash map))
        }

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }
    }

    companion object {

        const val FLAG = "flag"
        const val DIST = "dist"
        const val SERVICE = "service"
        const val BOOKING_FLAG = "ServiceBooking"

        fun newInstance(context: Context,  map: HashMap<String, Any>, dist: String) = Intent(context, ServiceSingleActivity::class.java).apply {
            putExtra(FLAG, map)
            putExtra(DIST, dist)
        }
    }
}