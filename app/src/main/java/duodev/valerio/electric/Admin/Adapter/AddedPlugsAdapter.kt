package duodev.valerio.electric.Admin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Connector

class AddedPlugsAdapter(
    private val list: MutableList<Connector>,
    private val listener: OnClick
): BaseRecyclerViewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_added_plug, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addAll(item: MutableList<Connector>){
        list.addAll(item)
        notifyDataSetChanged()
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
            setPort(item.type,plugImage)
            removeButton.setOnClickListener {
                listener.onRemoved(item, adapterPosition)
                removeItem(adapterPosition)
            }
        }
    }
    private fun setPort(portType: String, portIcon: ImageView ) {
        when (portType) {
            CHAD_DC -> portIcon.setImageResource(R.drawable.ic_chad_icon)
            CCS_DC -> portIcon.setImageResource(R.drawable.ic_ccs_icon)
            TYPE_TWO_43 -> portIcon.setImageResource(R.drawable.ic_type_two_icon)
            TYPE_TWO_22 -> portIcon.setImageResource(R.drawable.ic_type_two_icon)
            TYPE_ONE_7 -> portIcon.setImageResource(R.drawable.ic_type_one_icon)
            IEC_PLUG -> portIcon.setImageResource(R.drawable.ic_iec_plug)
            AMP_16 -> portIcon.setImageResource(R.drawable.ic_type_one_icon)
        }
    }
    interface OnClick {
        fun  onRemoved(item: Connector, position: Int)
    }
}