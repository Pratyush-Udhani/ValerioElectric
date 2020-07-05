package duodev.valerio.electric.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.addFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        setUpFragments()
    }

    private fun setUpFragments() {
        addFragment(null, R.id.loginContainer, LogInFragment.newInstance(), this)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
