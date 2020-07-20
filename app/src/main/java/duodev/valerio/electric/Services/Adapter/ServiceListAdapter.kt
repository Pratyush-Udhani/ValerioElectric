package duodev.valerio.electric.Services.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.Utils.miles2km
import duodev.valerio.electric.pojos.Station

class ServiceListAdapter(
    private val list: LinkedHashMap<Station, String>,
    private val listener: OnClick
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_service, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ServiceListAdapter.ViewHolder -> holder.bindItems(
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

        private val serviceStationName: TextView = itemView.findViewById(R.id.serviceStation_name)
        private val serviceStationAddress: TextView = itemView.findViewById(R.id.serviceStation_address)
        private val cardView: CardView = itemView.findViewById(R.id.serviceStation_cardView)
        private val distLabel: TextView = itemView.findViewById(R.id.serviceStation_distance)

        fun bindItems(item: Station, distance: String) {

            serviceStationName.text = item.stationAddress
            serviceStationAddress.text = item.stationLocation
            distLabel.text = "${miles2km(distance.toDouble())} km"
            cardView.setOnClickListener {
                listener.onServiceClicked(item, miles2km(distance.toDouble()).toString())
            }
        }
    }

    interface OnClick {
        fun onServiceClicked(station: Station, dist: String)
    }


}