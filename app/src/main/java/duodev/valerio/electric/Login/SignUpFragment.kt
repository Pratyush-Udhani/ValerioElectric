package duodev.valerio.electric.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import duodev.valerio.electric.Login.ViewModel.LoginViewModel
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Users
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    private val loginViewModel = LoginViewModel()

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
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
        setUpUI()
    }

    private fun setUpUI() {
        retypePassword.addTextChangedListener {
            if (retypePassword.text.trim().toString() != userPassword.text.trim().toString()) {
                passwordMatchText.makeVisible()
            } else {
                passwordMatchText.makeGone()
            }
        }

        userPhone.addTextChangedListener {
            if (userPhone.text.length == 10) {
                closeKeyboard(requireContext(), userPhone)
            }
        }
    }

    private fun setListeners() {
        signIn.setOnClickListener {
            replaceFragment(this, R.id.loginContainer, LogInFragment.newInstance())
        }
        signUpButton.setOnClickListener {
            setUpAccount()
        }
    }

    private fun setUpAccount() {
        if (userName.text.isNotEmpty()
            && userEmail.text.isNotEmpty()
            && userPassword.text.isNotEmpty()
            && retypePassword.text.isNotEmpty()
            && userPhone.text.isNotEmpty()
            && userAddress.text.isNotEmpty()
        ) {
            if (retypePassword.text.trim().toString() == userPassword.text.trim().toString()) {
                if (userPhone.text.length == 10) {
                    Users(
                        name = userName.text.toString(),
                        email = userEmail.text.toString(),
                        contact = userPhone.text.toString(),
                        address = userAddress.text.toString(),
                        hash = generateHash(userPassword.text.toString())
                    ).also {
                        loginViewModel.setUser(users = it)
                    }
                    activity?.toast("account added")
                    replaceFragment(this, R.id.loginContainer, LogInFragment.newInstance())
                } else {
                    activity?.toast("Enter a valid phone number")
                }
            } else
                activity?.toast("Passwords do not match")
        } else {
            activity?.toast("Enter all details please")
        }
    }


    companion object {
        fun newInstance() = SignUpFragment()
    }
}