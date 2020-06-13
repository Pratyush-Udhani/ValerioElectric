package duodev.valerio.electric.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {


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
    }

    private fun setListeners() {
        signIn.setOnClickListener {
            replaceFragment(this, R.id.loginContainer, LogInFragment.newInstance())
        }
        signUpButton.setOnClickListener {
            replaceFragment(this, R.id.loginContainer, LogInFragment.newInstance())
        }
    }


    companion object {
        fun newInstance() = SignUpFragment()
    }
}