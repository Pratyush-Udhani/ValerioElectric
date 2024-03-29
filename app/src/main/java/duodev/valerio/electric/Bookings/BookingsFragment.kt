package duodev.valerio.electric.Bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.toast
import duodev.valerio.electric.base.BaseFragment

class BookingsFragment : BaseFragment() {

    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }

    companion object {
        fun newInstance() = BookingsFragment()
    }
}