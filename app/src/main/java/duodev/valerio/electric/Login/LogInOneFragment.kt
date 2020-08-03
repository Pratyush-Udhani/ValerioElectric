package duodev.valerio.electric.Login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Users
import kotlinx.android.synthetic.main.fragment_log_in_one.*


class LogInOneFragment : BaseFragment() {

    private lateinit var user: Users
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(FLAG)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in_one, container, false)
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
        userName.setText(user.name)
        userPhone.addTextChangedListener {
            if (userPhone.text.length == 10) {
                closeKeyboard(requireContext(), userPhone)
            }
        }
    }

    private fun setListeners() {

        loginButton.setOnClickListener {
            if (userName.text.isNotEmpty() && userPhone.text.isNotEmpty()) {
                loader.makeVisible()
                user.name = userName.text.toString()
                user.address = userAddress.text.toString()
                user.contact = userPhone.text.toString()
                pm.setUser(user)
                pm.account = true

                firebaseFirestore.collection(USERS).document(pm.email.trim())
                    .set(user).addOnSuccessListener {
                        startActivity(HomeActivity.newInstance(requireContext(), USERS))
                        activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
                        loader.makeGone()
                    }
            } else {
                requireContext().toast("Enter all details")
            }
        }
    }

    companion object {

        private const val FLAG = "flag"

        fun newInstance(user: Users) = LogInOneFragment().apply {
            arguments = Bundle().apply {
                putParcelable(FLAG, user)
            }
        }
    }
}