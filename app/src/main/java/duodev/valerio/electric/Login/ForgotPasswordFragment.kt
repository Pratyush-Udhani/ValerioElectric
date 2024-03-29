package duodev.valerio.electric.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        cancelButton.setOnClickListener {
            replaceFragment(this, R.id.loginContainer ,LogInFragment.newInstance())
        }

        submitButton.setOnClickListener {
            replaceFragment(this, R.id.loginContainer ,LogInFragment.newInstance())
        }
    }


    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }
}