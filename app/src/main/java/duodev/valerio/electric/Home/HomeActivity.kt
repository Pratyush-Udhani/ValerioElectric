package duodev.valerio.electric.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import duodev.valerio.electric.R
import duodev.valerio.electric.Station.StationListFragment
import duodev.valerio.electric.Utils.addFragment

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
        addFragment(null, R.id.homeContainer, HomeMapFragment.newInstance(), this)
    }

    private fun setListeners() {

    }
}