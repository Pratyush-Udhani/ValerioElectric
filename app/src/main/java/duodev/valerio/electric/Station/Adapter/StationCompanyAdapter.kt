package duodev.valerio.electric.Station.Adapter


import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.switchmaterial.SwitchMaterial
import duodev.valerio.electric.R
import duodev.valerio.electric.base.BaseRecyclerViewAdapter
import duodev.valerio.electric.pojos.Company


class StationCompanyAdapter(
    private val list: MutableList<Company>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter() {
    private val array = SparseBooleanArray()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_company, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bindItems(list[position],position)
            }
        }
    }
    fun addChecked(data: ArrayList<String>){
        for( i in data) {
            array.put(list.indexOfFirst { it.name == i },true)
        }
        notifyDataSetChanged()
    }
    fun addItems(data: List<Company>){
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Company, position: Int) {
            val companyName: TextView = itemView.findViewById(R.id.companyName)
            val companyImage: ImageView = itemView.findViewById(R.id.companyLogo2)
            val companySwitch: SwitchMaterial = itemView.findViewById(R.id.switchCompany)
            companyName.text = item.name
            if(item.imageUri.isNotEmpty()) {
                Glide.with(getContext())
                    .load(item.imageUri)
//                    .transition(DrawableTransitionOptions.withCrossFade())
                    .dontTransform()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(companyImage)
            }
            companySwitch.isChecked = array[position]
            companySwitch.setOnClickListener {
                array.put(position,true);
                listener.onCheckedChanged(item,(it as SwitchMaterial).isChecked)
            }
//            companySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//                array.put(position,true);
//                listener.onCheckedChanged(item,isChecked)
//            }


        }
    }

    interface OnClick {
//        fun onClick(v: View?)
        fun onCheckedChanged(company: Company, isChecked: Boolean)
    }


}