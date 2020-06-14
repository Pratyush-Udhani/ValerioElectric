package duodev.valerio.electric.Station

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.Home.HomeMapFragment
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_station_single.*

class StationSingleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                replaceFragment(
                    this@StationSingleFragment,
                    R.id.homeContainer,
                    HomeMapFragment.newInstance())
            }
        })
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station_single, container, false)
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
            replaceFragment(this, R.id.homeContainer, HomeMapFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = StationSingleFragment()
    }
}