package duodev.valerio.electric.Bookings.Adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Connector

class BookingPlugAdapter(
    private val list: MutableList<Connector>,
    private val listener: OnClick
): BaseRecyclerViewAdapter() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_plug, parent))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<Connector>){
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Connector) {
            val portName: TextView = itemView.findViewById(R.id.plugName)
            val portPrice: TextView = itemView.findViewById(R.id.plugPower)
            val portImage: ImageView = itemView.findViewById(R.id.plugLogo)
            val portCard: CardView = itemView.findViewById(R.id.plugCard)
            val view: View = itemView.findViewById(R.id.view)

            portName.text = item.type
            portPrice.text = "Rs. ${item.price.toDouble()} per kWh"

            when (item.type.trimString()) {
                CHAD -> portImage.setImageResource(R.drawable.ic_chad_icon)
                CCS -> portImage.setImageResource(R.drawable.ic_ccs_icon)
                TYPE_TWO -> portImage.setImageResource(R.drawable.ic_type_two_icon)
                TYPE_TWO_22 -> portImage.setImageResource(R.drawable.ic_type_two_icon)
                TYPE_ONE_7 -> portImage.setImageResource(R.drawable.ic_type_one_icon)
                IEC_PLUG -> portImage.setImageResource(R.drawable.ic_iec_plug)
                AMP_16 -> portImage.setImageResource(R.drawable.ic_type_one_icon)
            }

            if (selectedPosition == adapterPosition) {
                portCard.setCardBackgroundColor(Color.parseColor("#0072bc"))
                portName.setTextColor(Color.WHITE)
                portPrice.setTextColor(Color.WHITE)
                view.setBackgroundColor(Color.parseColor("#EBEBEB"))
                portImage.setColorFilter(Color.WHITE)
            } else {
                portCard.setCardBackgroundColor(Color.WHITE)
                portName.setTextColor(Color.parseColor("#0072bc"))
                portPrice.setTextColor(Color.parseColor("#0072bc"))
                view.setBackgroundColor(Color.parseColor("#979797"))
                portImage.setColorFilter(Color.parseColor("#0072bc"))
            }

            portCard.setOnClickListener {
                portCard.setCardBackgroundColor(Color.parseColor("#0072bc"))
                portName.setTextColor(Color.WHITE)
                portPrice.setTextColor(Color.WHITE)
                view.setBackgroundColor(Color.parseColor("#EBEBEB"))
                portImage.setColorFilter(Color.WHITE)

                if (selectedPosition != -1)
                    notifyItemChanged(selectedPosition)
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition)
                listener.onItemClicked(item)
            }

        }
    }

    interface OnClick {
        fun onItemClicked(ports: Connector)
    }
}