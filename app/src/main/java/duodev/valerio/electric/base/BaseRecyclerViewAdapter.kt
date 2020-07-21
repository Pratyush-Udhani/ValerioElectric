package duodev.valerio.electric.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    protected fun getContext() = context

    protected fun getView(@LayoutRes layoutRes: Int, viewGroup: ViewGroup) : View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutRes, viewGroup, false)
    }
}