package duodev.valerio.electric.Admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.addFragment

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        init()
    }

    private fun init() {
        setUpFragment()
    }

    private fun setUpFragment() {
        addFragment(null,R.id.adminContainer, AdminPanelFragment.newInstance(), this)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, AdminActivity::class.java)
    }
}