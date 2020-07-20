package duodev.valerio.electric.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_home_map.*

class HomeMapFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private var backPressed: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpMap()
        setListeners()
    }

    private fun setListeners() {
        gotToButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, StationListFragment.newInstance())
        }
        stationLayout.setOnClickListener {
//            startActivity(StationSingleActivity.newInstance(requireContext()))
//            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }
    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        fun newInstance() = HomeMapFragment()
    }
}