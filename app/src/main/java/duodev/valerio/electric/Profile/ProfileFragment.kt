package duodev.valerio.electric.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Login.LoginActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.PreferenceUtils
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        logoutButton.setOnClickListener {
            initiateSignOut()
        }
        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun initiateSignOut() {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (account != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            val googleSignInClient: GoogleSignInClient =
                GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut().addOnSuccessListener {
                startActivity(LoginActivity.newInstance(requireContext()))
                pm.resetUser()
            }
        } else {
            startActivity(LoginActivity.newInstance(requireContext()))
            pm.resetUser()
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}