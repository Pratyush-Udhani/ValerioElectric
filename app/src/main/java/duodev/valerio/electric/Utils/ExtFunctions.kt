package duodev.valerio.electric.Utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast

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