package duodev.valerio.electric.Station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.pojos.Station

class StationListAdapter(private val list: MutableList<Station>, private val listener: OnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.station_card,
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
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Station) {
            val stationName: TextView = itemView.findViewById(R.id.stationName)
            val stationAddress: TextView = itemView.findViewById(R.id.stationAddress)
            val stationAvailability: TextView = itemView.findViewById(R.id.stationAvailability)
            val starred: ImageView = itemView.findViewById(R.id.StationStar)

            stationName.text = item.stationName
            stationAddress.text = item.stationAddress
            stationAvailability.text = if (item.stationAvailability) "Available" else "Unavailable"
            if (item.starred){
                starred.setImageResource(R.drawable.ic_star_selected)
            } else {
                starred.setImageResource(R.drawable.ic_star_unselected)

            }

        }
    }
    interface OnClick{

    }
}