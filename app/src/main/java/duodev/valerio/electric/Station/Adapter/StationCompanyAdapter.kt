package duodev.valerio.electric.Station.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.valerio.electric.R
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Company

class StationCompanyAdapter(
    private val list: MutableList<Company>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_company, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addItems(data: List<Company>){
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Company) {
            val companyName: TextView = itemView.findViewById(R.id.companyName)
            val companyImage: ImageView = itemView.findViewById(R.id.companyLogo)

            companyName.text = item.name
        }
    }

    interface OnClick {

    }


}