package duodev.valerio.electric.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Login.LoginActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.Utils.USER
import duodev.valerio.electric.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : BaseActivity() {

    private val SPLASH_SCREEN_TIMEOUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        animateViews()
        handleLogin()
    }

    private fun animateViews() {
        splashBackground.animate().alpha(1f).duration = SPLASH_SCREEN_TIMEOUT
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun handleLogin() {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        Handler().postDelayed({
            if (account!=null || pm.account) {
                startActivity(HomeActivity.newInstance(this, USER))
                overridePendingTransition(
                    R.anim.slideleft,
                    R.anim.slideright
                )
            } else {
                startActivity(LoginActivity.newInstance(this))
                overridePendingTransition(
                    R.anim.slideleft,
                    R.anim.slideright
                )
            }
        }, SPLASH_SCREEN_TIMEOUT)
    }
}
