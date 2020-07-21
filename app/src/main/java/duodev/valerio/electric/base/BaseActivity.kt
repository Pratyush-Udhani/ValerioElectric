package duodev.valerio.electric.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import duodev.valerio.electric.Utils.PreferenceUtils

abstract class BaseActivity: AppCompatActivity() {
    val pm = PreferenceUtils
}