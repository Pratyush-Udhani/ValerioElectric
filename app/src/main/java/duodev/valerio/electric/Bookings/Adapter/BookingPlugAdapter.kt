package duodev.valerio.electric.Bookings.Adapter

import android.graphics.Color
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

    private var selectedPosition = -1

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
            val view: View = itemView.findViewById(R.id.view)

            portName.text = item.portName
            portPrice.text = item.portCost
            portImage.setImageResource(item.portImageRes)


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
        fun onItemClicked(ports: Ports)
    }
}