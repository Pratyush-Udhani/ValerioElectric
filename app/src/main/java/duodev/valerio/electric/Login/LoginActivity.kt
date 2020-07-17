package duodev.valerio.electric.Login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import duodev.valerio.electric.Admin.AdminActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.Utils.trimString
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_admin_login.*

class LoginActivity : AppCompatActivity() {

    private val dialog by lazy { Dialog(this) }

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
                    startActivity(AdminActivity.newInstance(this@LoginActivity))
                    overridePendingTransition(R.anim.slideleft, R.anim.slideright)
                    dismiss()
                } else {
                    this@LoginActivity.toast("Invalid password")
                }
            }
            show()
        }

    }

    private fun setUpFragments() {
        addFragment(null, R.id.loginContainer, LogInFragment.newInstance(), this)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
