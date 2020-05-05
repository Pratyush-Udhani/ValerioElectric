package duodev.valerio.electric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import duodev.valerio.electric.Login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signIn()
    }

    private fun signIn() {
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
