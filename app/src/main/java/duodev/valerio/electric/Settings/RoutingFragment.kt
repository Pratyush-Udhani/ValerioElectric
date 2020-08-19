package duodev.valerio.electric.Settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import duodev.valerio.electric.Admin.AddStationLocation
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Login.LoginActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Settings.RoutingFragment.Companion.newInstance
import duodev.valerio.electric.Utils.PICK_COMPANY_IMAGE
import duodev.valerio.electric.Utils.PICK_STATION_IMAGE
import duodev.valerio.electric.Utils.makeGone
import duodev.valerio.electric.Utils.makeVisible
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_contact_us.backButton
import kotlinx.android.synthetic.main.fragment_contact_us.emailLayout
import kotlinx.android.synthetic.main.fragment_routing.*
import kotlinx.android.synthetic.main.layout_add_station.*

class RoutingFragment : BaseFragment() {
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var isStartingPointBtnPressed: Boolean = false;
    private var isDestinationPointBtnPressed: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        setListeners()
    }

    private fun setListeners() {

        startingButton.setOnClickListener {
            isStartingPointBtnPressed = true;
            startActivityForResult(
                AddStationLocation.newInstance(requireContext()),
                AdminPanelFragment.CODE
            )
            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        destinationButton.setOnClickListener {
            isDestinationPointBtnPressed = true;
            startActivityForResult(
                AddStationLocation.newInstance(requireContext()),
                AdminPanelFragment.CODE
            )
            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        createRouteButton.setOnClickListener {
            val uri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&origin=${startingPoint.text}&destination=${destinationPoint.text}&waypoints=Jodhpur")
            val i = Intent(Intent.ACTION_VIEW, uri)
            i.setPackage("com.google.android.apps.maps")
            startActivity(i)
        }

        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
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
                    startingPoint.setText(data.getStringExtra(AddStationLocation.ADDRESS))
                    isStartingPointBtnPressed = false;
                }
                if (isDestinationPointBtnPressed) {
                    destinationPoint.setText(data.getStringExtra(AddStationLocation.ADDRESS))
                    isDestinationPointBtnPressed = false;
                }
                Log.d("TATATATA", "$lat, $long")
            }
        }
    }


    companion object {
        fun newInstance() = RoutingFragment()
    }
}