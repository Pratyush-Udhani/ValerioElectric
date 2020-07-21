package duodev.valerio.electric.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.Admin.AdminPanelFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.USER
import duodev.valerio.electric.Utils.replaceFragment
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        addStationButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, AdminPanelFragment.newInstance(USER))
        }
        changePasswordButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, SettingsChangePassword.newInstance())
        }
        contactUsButton.setOnClickListener {
            replaceFragment(this, R.id.homeContainer, ContactUsFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}