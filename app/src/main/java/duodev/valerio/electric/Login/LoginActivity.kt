package duodev.valerio.electric.Login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private val dialog by lazy { Dialog(this) }
    lateinit var currentFragment: Fragment
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        setUpFragments()
        setListeners()
    }

    private fun setListeners() {
        logoCard.setOnLongClickListener {
            showDialog()
            return@setOnLongClickListener true
        }
    }

    private fun showDialog(){
        val view: View = layoutInflater.inflate(R.layout.dialog_admin_login, null)
        dialog.apply {
            setContentView(view)
            create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val adminPassword: EditText = view.findViewById(R.id.adminPassword)
            val loginButton: CardView = view.findViewById(R.id.adminLoginButton)

            loginButton.setOnClickListener {
                if (adminPassword.trimString() == "12345") {
                    startActivity(HomeActivity.newInstance(this@LoginActivity, ADMIN))
                    overridePendingTransition(R.anim.slideleft, R.anim.slideright)
                    dismiss()
                } else {
                    this@LoginActivity.toast("Invalid password")
                }
            }
            show()
        }

    }

    override fun onBackPressed() {
        currentFragment = supportFragmentManager.findFragmentById(R.id.loginContainer)!!
        if (currentFragment is SignUpFragment || currentFragment is ForgotPasswordFragment) {
            supportFragmentManager.popBackStackImmediate()
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

    private fun setUpFragments() {
        addFragment(null, R.id.loginContainer, LogInFragment.newInstance(), this)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
