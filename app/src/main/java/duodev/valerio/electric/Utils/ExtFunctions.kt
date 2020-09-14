package duodev.valerio.electric.Utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import duodev.valerio.electric.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

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

fun replaceFragment(fromFragment : Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null, tag:Boolean = false) {
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

fun addFragment(fromFragment: Fragment?, container: Int, fragment: Fragment, activity: FragmentActivity? = null, tag: Boolean = false) {
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

fun closeKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun miles2km (distInMiles: Double): Int {
    return BigDecimal(distInMiles * 1.60934).setScale(2, RoundingMode.HALF_EVEN).toInt()
}

fun TextView.trimString(): String = text.toString().trim()

fun EditText.trimString(): String = text.toString().trim()

fun String.trimString(): String = this.trim()

fun generateHash(password: String): String {
    val bytes = password.toByteArray()
    val mb = MessageDigest.getInstance("SHA-256")
    val digest = mb.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun long2Date(date: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(Date(date))
}

fun long2time(time: Long): String {
    val formatter = SimpleDateFormat("h:mm a")
    return formatter.format(Date(time))
}

fun roundOffTwoDigits(number: Any): String? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING

    return df.format(number)
}

private fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}

fun distance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val theta = lon1 - lon2
    var dist = (sin(deg2rad(lat1))
            * sin(deg2rad(lat2))
            + (cos(deg2rad(lat1))
            * cos(deg2rad(lat2))
            * cos(deg2rad(theta))))
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    return dist
}








