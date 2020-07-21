package duodev.valerio.electric.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings_change_password.*


class SettingsChangePassword : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
        setUpListeners()
    }

    private fun setUpListeners() {
        submitButton.setOnClickListener {
            if (!passwordMatchText.isVisible) {
                checkAuth(pm.email, currentPassword.text.toString())
                val hash  = generateHash(newPassword.text.toString())
                isAuth.observe(viewLifecycleOwner, Observer {
                    if (isAuth.value!!){
                        changePassword(hash)
                        currentPassword.setText("")
                        newPassword.setText("")
                        retypeNewPassword.setText("")
                        activity?.toast("Password changes successfully")
                    }
                })

                noPassword.observe(viewLifecycleOwner, Observer {
                    if (noPassword.value!!) {
                        activity?.toast("Invalid Password")
                    }
                })
            } else {
                activity?.toast("Passwords do not match")
            }
        }

        cancelButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }

        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun setUpUI() {
        if (pm.hash == generateHash("0000")) {
            currentPassword.setText("0000")
        }

        retypeNewPassword.addTextChangedListener {
            passwordMatchText.isVisible = it.toString() != newPassword.text.toString()
        }
    }

    companion object {
        fun newInstance() = SettingsChangePassword()
    }
}