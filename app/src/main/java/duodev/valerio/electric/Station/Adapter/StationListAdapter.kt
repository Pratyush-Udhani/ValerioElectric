package duodev.valerio.electric.Station.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Station

class StationListAdapter(
    private val list: LinkedHashMap<Station, String>,
    private val listener: OnClick
) :
    BaseRecyclerViewAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(getView(R.layout.card_station, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(
                list.keys.elementAt(position),
                list.values.elementAt(position)
            )
        }
    }

    fun addData(data: MutableMap<Station, String>) {
        list.putAll(data)
        log("called")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val stationName: TextView = itemView.findViewById(R.id.stationName)
        private val stationAddress: TextView = itemView.findViewById(R.id.stationAddress)
        private val cardView: CardView = itemView.findViewById(R.id.stationCard)
        private val distLabel: TextView = itemView.findViewById(R.id.distanceLabel)
        private val portsLayout: LinearLayout = itemView.findViewById(R.id.portsLayout)
        private val singlePortLayout1: LinearLayout = itemView.findViewById(R.id.singlePortLayout1)
        private val singlePortLayout2: LinearLayout = itemView.findViewById(R.id.singlePortLayout2)
        private val extrasText: TextView = itemView.findViewById(R.id.plusMoreText)
        private val portIcon1: ImageView = itemView.findViewById(R.id.portIcon1)
        private val portIcon2: ImageView = itemView.findViewById(R.id.portIcon2)
        private val portName1: TextView = itemView.findViewById(R.id.portName1)
        private val portName2: TextView = itemView.findViewById(R.id.portName2)
        private val stationImage: ImageView = itemView.findViewById(R.id.stationImage)

        fun bindItems(item: Station, distance: String) {

//            stationName.text = item.stationAddress
//            stationAddress.text = item.stationLocation
            stationAddress.text = item.stationAddress
            distLabel.text = "${miles2km(distance.toDouble())} km"
            Glide.with(getContext()).load(item.ownerCompany.imageUri).into(stationImage)

//            portName1.text = item.connectorType[0].type
//            portName2.text = item.connectorType[1].type

            if(item.connectorType.size >= 2 ) {
                setPort(item.connectorType[0].type, portIcon1, portName1);
                setPort(item.connectorType[1].type, portIcon2, portName2);
            }

            when (item.connectorType.size) {
                0 -> portsLayout.makeGone()
                1 -> {
                    portsLayout.makeVisible()
                    extrasText.makeGone()
                    singlePortLayout2.makeGone()
                    setPort(item.connectorType[0].type, portIcon1, portName1);
                }
                2 -> {
                    portsLayout.makeVisible()
                    extrasText.makeGone()
                }
                else -> {
                    portsLayout.makeVisible()
                    extrasText.makeVisible()
                    extrasText.text = "+${item.connectorType.size - 2} more"
                }
            }

            cardView.setOnClickListener {
                listener.onStationClicked(item, miles2km(distance.toDouble()).toString())
            }
        }
    }

    interface OnClick {
        fun onStationClicked(station: Station, dist: String)
    }

    private fun setPort(portType: String, portIcon: ImageView, portName: TextView ) {
        portName.text = portType;
        when (portType) {
            CHAD_DC -> portIcon.setImageResource(R.drawable.ic_chad_icon)
            CCS_DC -> portIcon.setImageResource(R.drawable.ic_ccs_icon)
            TYPE_TWO_43 -> portIcon.setImageResource(R.drawable.ic_type_two_icon)
            TYPE_TWO_22 -> portIcon.setImageResource(R.drawable.ic_type_two_icon)
            TYPE_ONE_7 -> portIcon.setImageResource(R.drawable.ic_type_one_icon)
            IEC_PLUG -> portIcon.setImageResource(R.drawable.ic_iec_plug)
            AMP_16 -> portIcon.setImageResource(R.drawable.ic_type_one_icon)
        }
    }
}