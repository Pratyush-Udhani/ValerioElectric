package duodev.valerio.electric.Utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun log(message: String) {
    Log.d("TAG!!!!", message)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun replaceFragment(fromFragment : Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null) {
    when {
        activity != null -> {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        fromFragment != null -> {
            val fragmentTransaction = fromFragment.activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        else -> {
            log("Wrong call")
        }
    }
}

fun addFragment(fromFragment: Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null) {
    when {
        activity != null -> {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.add(container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        fromFragment != null -> {
            val fragmentTransaction = fromFragment.activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.add(container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        else -> {
            log("Wrong call")
        }
    }
}