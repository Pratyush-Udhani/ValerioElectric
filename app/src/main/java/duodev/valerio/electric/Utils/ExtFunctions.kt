package duodev.valerio.electric.Utils

import android.app.Activity
import android.content.Context
import android.hardware.input.InputManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.MessageDigest

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

fun String.trimString(): String = this.trim()

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun replaceFragment(fromFragment : Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null, tag:Boolean = false) {
    when {
        activity != null -> {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(container, fragment)
            if (tag)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        fromFragment != null -> {
            val fragmentTransaction = fromFragment.activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(container, fragment)
            if (tag)
                fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        else -> {
            log("Wrong call")
        }
    }
}

fun addFragment(fromFragment: Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null, tag: Boolean = false) {
    when {
        activity != null -> {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.add(container, fragment)
            if (tag)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        fromFragment != null -> {
            val fragmentTransaction = fromFragment.activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.add(container, fragment)
            if (tag)
                fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        else -> {
            log("Wrong call")
        }
    }
}

fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun miles2km (distInMiles: Double): Int {
    return BigDecimal(distInMiles * 1.60934).setScale(2, RoundingMode.HALF_EVEN).toInt()
}

fun generateHash(password: String): String {
    val bytes = password.toByteArray()
    val mb = MessageDigest.getInstance("SHA-256")
    val digest = mb.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}













