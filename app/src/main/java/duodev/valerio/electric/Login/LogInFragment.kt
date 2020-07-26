package duodev.valerio.electric.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Users
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : BaseFragment() {

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObservers()
        setListeners()
    }

    private fun setUpObservers() {
        isAuth.observe(viewLifecycleOwner, Observer {
            if (isAuth.value!!) {
                pm.email = userEmail.text.toString()
                pm.account = true
                isAuth.value = false
                startActivity(HomeActivity.newInstance(requireContext(), USER))
            }
        })

        noPassword.observe(viewLifecycleOwner, Observer {
            if (noPassword.value!!) {
                activity?.toast("Invalid password")
            }
        })

        noEmail.observe(viewLifecycleOwner, Observer {
            if (noEmail.value!!) {
                activity?.toast("No user found")
            }
        })
    }

    private fun setListeners() {
        sign_in_button.setOnClickListener {
            initiateLogin()
        }
        loginButton.setOnClickListener {
            if (userEmail.text.isNotEmpty() && userPassword.text.isNotEmpty()) {
                checkAuth(userEmail.text.toString(), userPassword.text.toString())
            } else {
                activity?.toast("Enter details please")
            }
        }

        signUp.setOnClickListener {
            replaceFragment(this, R.id.loginContainer, SignUpFragment.newInstance())
        }

        forgotPasswordButton.setOnClickListener {
            replaceFragment(this, R.id.loginContainer, ForgotPasswordFragment.newInstance())
        }

    }

    private fun initiateLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInGoogle: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogle, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            Log.d("SIGNUP", "result caode")
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            Log.d("SIGNUP", "success")
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            loader.makeVisible()
            proceedWithLogin(account = account)
        } catch (e: ApiException) {
            Log.d("SIGNUP", "fail + $e")

        }
    }

    private fun proceedWithLogin(account: GoogleSignInAccount?) {
        var intent: Intent? = null
        Log.d("SIGNUP", "here")
        firebaseFirestore.collection(USERS).document(account?.email.toString().trim()).get()
            .addOnSuccessListener {
                Log.d("SIGNUP", "SUCCESS")
                pm.name = account?.givenName.toString()
                pm.email = account?.email.toString()
                pm.account = true
                if (it.exists()) {
                    pm.setUser(convertToPojo(it.data!!, Users::class.java))
                    intent = HomeActivity.newInstance(requireContext(), USER)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
                } else {
                    Log.d("SIGNUP", "NEW")
                    firebaseFirestore.collection(USERS).document(account?.email.toString().trim())
                        .set(pm.getUser())
                    loader.makeGone()
                    intent = HomeActivity.newInstance(requireContext(), USER)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
                }
            }
    }

    companion object {
        fun newInstance() = LogInFragment()
    }
}