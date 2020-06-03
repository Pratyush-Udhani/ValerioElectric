package duodev.valerio.electric.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import duodev.valerio.electric.R

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
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.loginContainer,
            LogInFragment.newInstance(),
            "Log in"
        )
        fragmentTransaction.commit()
    }
}
