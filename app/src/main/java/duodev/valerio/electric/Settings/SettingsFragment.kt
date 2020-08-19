package duodev.valerio.electric.Settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.Profile.ProfileFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Services.BookingServiceFragment
import duodev.valerio.electric.Station.BookingStationFragment
import duodev.valerio.electric.Utils.USER
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        routingButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, RoutingFragment.newInstance())
        }


        addStationButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, AdminPanelFragment.newInstance(USER))
        }

        changePasswordButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, SettingsChangePassword.newInstance())
        }

        contactUsButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, ContactUsFragment.newInstance())
        }

        profileButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, ProfileFragment.newInstance())
        }

        conditionsButton.setOnClickListener {
            val i = Intent(context, ConditionsActivity::class.java)
            startActivity(i)
            allowEnterTransitionOverlap
        }

        showBookingsButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, BookingServiceFragment.newInstance())
        }


        showStationBookingsButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, BookingStationFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}