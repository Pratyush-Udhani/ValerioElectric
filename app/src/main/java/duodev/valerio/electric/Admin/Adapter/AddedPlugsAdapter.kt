package duodev.valerio.electric.Admin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.pojos.Connector

class AddedPlugsAdapter(
    private val list: MutableList<Connector>,
    private val listener: OnClick
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_added_plug, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addItem(item: Connector) {
        list.add(0, item)
        notifyItemInserted(0)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAll() {
        list.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val plugName : TextView = itemView.findViewById(R.id.plugName)
        private val plugPrice: TextView = itemView.findViewById(R.id.plugPrice)
        private val plugImage: ImageView = itemView.findViewById(R.id.plugImage)
        private val removeButton: ImageView = itemView.findViewById(R.id.removeButton)

        fun bindItems(item: Connector) {
            plugName.text = item.type
            plugPrice.text = "Rs. ${item.price} per hr"

            removeButton.setOnClickListener {
                listener.onRemoved(item, adapterPosition)
                removeItem(adapterPosition)
            }
        }
    }

    interface OnClick {
        fun  onRemoved(item: Connector, position: Int)
    }
}