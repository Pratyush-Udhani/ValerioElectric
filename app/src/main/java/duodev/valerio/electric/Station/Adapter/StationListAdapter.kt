package duodev.valerio.electric.Station.Adapter

import android.telephony.cdma.CdmaCellLocation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.log
import duodev.valerio.electric.pojos.Station

class StationListAdapter(private val list: MutableList<Station>, private val listener: OnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_station,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<Station>) {
        list.addAll(data)
        log("called")
        notifyDataSetChanged()
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val stationName: TextView = itemView.findViewById(R.id.stationName)
        private val stationAddress: TextView = itemView.findViewById(R.id.stationAddress)
        val stationAvailability: TextView = itemView.findViewById(R.id.stationAvailability)
        val starred: ImageView = itemView.findViewById(R.id.StationStar)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bindItems(item: Station) {

            stationName.text = item.stationName
            stationAddress.text = item.stationAddress

            stationName.setOnClickListener {
                log("called adapter")
                listener.onStationClicked(item)
            }

        }
    }
    interface OnClick{
        fun onStationClicked(station: Station)
    }
}