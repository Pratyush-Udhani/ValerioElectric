package duodev.valerio.electric.Station

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import duodev.valerio.electric.Payment.PaymentActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Company
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
        setUpStation()
    }

    private fun setUpStation(): Station {
        return Station(
            stationAddress = station[StationListFragment.ADDRESS].toString(),
            location = GeoPoint(station[StationListFragment.LATITUDE].toString().toDouble(), station[StationListFragment.LONGITUDE].toString().toDouble() ),
            stationLocation = station[StationListFragment.LOCATION].toString(),
            numberOfStations = station[StationListFragment.SLOTS].toString().toInt(),
            serviceProvider = station[StationListFragment.PROVIDER].toString(),
            imageUrl = station[StationListFragment.IMAGE_URL].toString(),
            stationId = station[StationListFragment.ID].toString(),
            ownerCompany = station[StationListFragment.OWNER] as Company,
            connectorType = station[StationListFragment.CONNECTOR] as List<Connector>,
            ownership = station[StationListFragment.OWNERSHIP].toString()
        )
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
        bookNowButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra(STATION, station)
            setResult(StationListFragment.RESULT_CODE, intent)
            finish()
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)

        }
    }


    companion object {

        const val FLAG = "flag"
        const val DIST = "dist"
        const val STATION = "Station"

        fun newInstance(context: Context, map: HashMap<String, Any>, dist: String) = Intent(context, StationSingleActivity::class.java).apply {
            putExtra(FLAG, map)
            putExtra(DIST, dist)
        }
    }
}