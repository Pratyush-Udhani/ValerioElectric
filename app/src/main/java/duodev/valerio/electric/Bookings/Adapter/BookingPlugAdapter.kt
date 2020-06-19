package duodev.valerio.electric.Bookings.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.pojos.Ports

class BookingPlugAdapter(
    private val list: MutableList<Ports>,
    private val listener: OnClick
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.plug_card,
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

            portName.text = item.portName
            portPrice.text = item.portCost

            Log.d("RES", item.portImageRes.toString())
            portImage.setImageResource(item.portImageRes)
        }
    }

    interface OnClick {

    }
}