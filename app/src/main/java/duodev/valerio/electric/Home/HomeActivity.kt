package duodev.valerio.electric.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.Bookings.BookingPlugsFragment
import duodev.valerio.electric.Bookings.BookingSlotFragment
import duodev.valerio.electric.Bookings.BookingsFragment
import duodev.valerio.electric.Profile.ProfileFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Settings.SettingsChangePassword
import duodev.valerio.electric.Settings.SettingsFragment
import duodev.valerio.electric.Station.StationCompanyFragment
import duodev.valerio.electric.Station.StationFilterFragment
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Utils.ADMIN
import duodev.valerio.electric.Utils.USERS
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    lateinit var currentFragment: Fragment
    private var backPressed: Long = 0
    private var flag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        intent?.let {
            flag = it.getStringExtra(FLAG)!!
        }
        init()
    }

    private fun init() {
        setListeners()
        setUpFragment()
    }

    private fun setUpFragment() {
        if (flag == ADMIN)
            addFragment(null, R.id.homeContainer, AdminPanelFragment.newInstance(flag), this)
        else
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

    override fun onBackPressed() {
        currentFragment = supportFragmentManager.findFragmentById(R.id.homeContainer)!!
        if (currentFragment is BookingPlugsFragment) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            if (currentFragment is BookingSlotFragment) {
                supportFragmentManager.popBackStackImmediate()
            } else {
                if (currentFragment is StationFilterFragment) {
                    supportFragmentManager.popBackStackImmediate()
                } else {
                    if (currentFragment is StationCompanyFragment) {
                        supportFragmentManager.popBackStackImmediate()
                    } else {
                        if (currentFragment is SettingsChangePassword) {
                            supportFragmentManager.popBackStackImmediate()
                        } else {
                            if (currentFragment is AdminPanelFragment) {
                                if (flag == ADMIN) {
                                    if (backPressed.plus(2000) >= System.currentTimeMillis()) {
                                        super.onBackPressed()
                                        finishAffinity()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Press again to exit",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        backPressed = System.currentTimeMillis()
                                    }
                                } else {
                                    supportFragmentManager.popBackStackImmediate()
                                }
                            } else {
                                if (backPressed.plus(2000) >= System.currentTimeMillis()) {
                                    super.onBackPressed()
                                    finishAffinity()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Press again to exit",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    backPressed = System.currentTimeMillis()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val FLAG = "flag"

        fun newInstance(context: Context, flag: String) = Intent(context, HomeActivity::class.java).apply {
            putExtra(FLAG, flag)
        }
    }
}