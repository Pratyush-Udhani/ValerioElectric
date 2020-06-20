package duodev.valerio.electric.Bookings.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.pojos.Ports

class BookingPlugAdapter(
    private val list: MutableList<Ports>,
    private val listener: OnClick
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.card_plug,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<Ports>){
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Ports) {
            val portName: TextView = itemView.findViewById(R.id.plugName)
            val portPrice: TextView = itemView.findViewById(R.id.plugCost)
            val portImage: ImageView = itemView.findViewById(R.id.plugLogo)
            val portCard: CardView = itemView.findViewById(R.id.plugCard)

            portName.text = item.portName
            portPrice.text = item.portCost
            portImage.setImageResource(item.portImageRes)

            portCard.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }

        }
    }

    interface OnClick {
        fun onItemClicked(position: Int)
    }
}