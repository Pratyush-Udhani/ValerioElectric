package duodev.valerio.electric.Station.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Station

class StationListAdapter(
    private val list: LinkedHashMap<Station, String>,
    private val listener: OnClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_station, parent, false))
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
        private val typeTwoLayout: LinearLayout = itemView.findViewById(R.id.typeTwoLayout)
        private val typeOneLayout: LinearLayout = itemView.findViewById(R.id.typeOneLayout)
        private val chadLayout: LinearLayout = itemView.findViewById(R.id.chadLayout)
        private val ccsLayout: LinearLayout = itemView.findViewById(R.id.ccsLayout)

        fun bindItems(item: Station, distance: String) {

            stationName.text = item.stationAddress
            stationAddress.text = item.stationLocation
            distLabel.text = "${miles2km(distance.toDouble())} km"

            for (element in item.connectorType) {
                when (element.type) {
                    TYPE_ONE -> typeOneLayout.makeVisible()
                    TYPE_TWO -> typeTwoLayout.makeVisible()
                    CHAD -> chadLayout.makeVisible()
                    CCS -> ccsLayout.makeVisible()
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
}