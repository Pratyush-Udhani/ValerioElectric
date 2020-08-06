package duodev.valerio.electric.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import duodev.valerio.electric.R
import kotlinx.android.synthetic.main.activity_conditions.*


class ConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conditions)
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)

        privacyPolicy.settings.javaScriptEnabled = true
        privacyPolicy.loadUrl("https://vecharge-bharat.flycricket.io/privacy.html")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        privacyPolicy.goBack()
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }
}