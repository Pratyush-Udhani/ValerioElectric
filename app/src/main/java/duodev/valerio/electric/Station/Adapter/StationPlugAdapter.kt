package duodev.valerio.electric.Station.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Connector

class StationPlugAdapter(
    private val list: MutableList<Connector>,
    private var slots: String
) : BaseRecyclerViewAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_station_plug, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<Connector>, slot: String) {
        list.addAll(data)
        slots = slot
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val plugName: TextView = itemView.findViewById(R.id.plugNameText)
        private val plugPrice: TextView = itemView.findViewById(R.id.portPrice)
        private val plugImage: ImageView = itemView.findViewById(R.id.plugIcon)
        private val slot: TextView = itemView.findViewById(R.id.portNumber)

        fun bindItems(item: Connector) {
            slot.text = item.ports
            plugName.text = item.type
            plugPrice.text = "Rs. ${item.price} per kWh"


            when (item.type) {
                CHAD -> plugImage.setImageResource(R.drawable.ic_chad_icon)
                CCS -> plugImage.setImageResource(R.drawable.ic_ccs_icon)
                TYPE_TWO -> plugImage.setImageResource(R.drawable.ic_type_two_icon)
                TYPE_TWO_22 -> plugImage.setImageResource(R.drawable.ic_type_two_icon)
                TYPE_ONE_7 -> plugImage.setImageResource(R.drawable.ic_type_one_icon)
                IEC_PLUG -> plugImage.setImageResource(R.drawable.ic_iec_plug)
                AMP_16 -> plugImage.setImageResource(R.drawable.ic_type_one_icon)
            }

        }
    }
}