package duodev.valerio.electric.Station

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.STATION
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.layout_add_station.view.*


class StationEditFragment : Fragment() {

    private lateinit var editStation:Station
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editStation = it.getParcelable<Station>(STATION)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
    }

    private fun setUpUI() {
        rootView = editStationLayout
        rootView.stationAddress.setText(editStation.stationAddress)
        rootView.headingText.text = "Edit Station"
//        rootView.stationSlots.setText()
    }

    companion object {

        fun newInstance(editStation: Station) =
            StationEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATION,editStation)

                }
            }
    }
}