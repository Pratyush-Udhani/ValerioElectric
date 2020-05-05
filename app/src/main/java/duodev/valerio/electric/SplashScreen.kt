package duodev.valerio.electric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

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

    private fun handleLogin() {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        Handler().postDelayed({
            if (account!=null) {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.slideleft, R.anim.slideright)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.slideleft, R.anim.slideright)
            }
        }, SPLASH_SCREEN_TIMEOUT)
    }
}
