package duodev.valerio.electric.Station

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.addFragment
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_station_filter.*
import kotlinx.android.synthetic.main.fragment_station_list.*
import kotlinx.android.synthetic.main.fragment_station_list.backButton

class StationFilterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpListeners()
    }

    private fun setUpListeners() {
        backButton.setOnClickListener {
            replaceFragment(
                this,
                R.id.homeContainer,
                StationListFragment.newInstance()
            )
        }
        companyList.setOnClickListener {
            addFragment(this, R.id.homeContainer, StationCompanyFragment.newInstance(), null, true)
        }
    }

    companion object {
        fun newInstance() = StationFilterFragment()
    }
}