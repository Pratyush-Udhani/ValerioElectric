package duodev.valerio.electric.Home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import duodev.valerio.electric.Bookings.BookingsFragment
import duodev.valerio.electric.Profile.ProfileFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Settings.SettingsFragment
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        setListeners()
        setUpFragment()
    }

    private fun setUpFragment() {
        addFragment(null, R.id.homeContainer, StationListFragment.newInstance(), this)
    }

    private fun setListeners() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationMap -> {
                    replaceFragment(null, R.id.homeContainer, StationListFragment.newInstance(), this)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigationClock -> {
                    replaceFragment(null, R.id.homeContainer, BookingsFragment.newInstance(), this)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigationProfile -> {
                    replaceFragment(null, R.id.homeContainer, ProfileFragment.newInstance(), this)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigationSettings -> {
                    replaceFragment(null, R.id.homeContainer, SettingsFragment.newInstance(), this)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }
}