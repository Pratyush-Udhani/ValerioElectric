package duodev.valerio.electric.Login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import duodev.valerio.electric.R
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 108

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
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        sign_in_button.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestEmail()
                .requestProfile()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            signIn()
        }
        signUp.setOnClickListener {
            changeFragment(SignUpFragment.newInstance())
        }

        forgotPasswordButton.setOnClickListener {
            changeFragment(ForgotPasswordFragment.newInstance())
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        mGoogleSignInClient.signOut()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Invalid Login", Toast.LENGTH_LONG).show()
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(
            R.id.loginContainer,
            fragment
        )
        fragmentTransaction?.commit()
    }

    companion object {
        fun newInstance() = LogInFragment()
    }
}