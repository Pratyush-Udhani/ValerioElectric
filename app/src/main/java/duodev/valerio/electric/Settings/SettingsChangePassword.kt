package duodev.valerio.electric.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.R
import duodev.valerio.electric.base.BaseFragment


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

    companion object {
        fun newInstance() = SettingsChangePassword()
    }
}