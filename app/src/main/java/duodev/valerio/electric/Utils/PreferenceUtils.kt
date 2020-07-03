package duodev.valerio.electric.Utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.common.util.SharedPreferencesUtils
import duodev.valerio.electric.ValerioApp

object PreferenceUtils {

    private val pm = PreferenceManager.getDefaultSharedPreferences(ValerioApp.instance)

    private const val NAME = "name"
    private const val EMAIL = "email"
    private const val MOBILE = "mobile"


    var name: String
        get() = pm.getString(NAME, "") ?: ""
        set(value) {
            pm.edit().putString(NAME, value).apply()
        }

    var email: String
        get() = pm.getString(EMAIL, "") ?: ""
        set(value) {
            pm.edit().putString(EMAIL, value).apply()
        }

    var mobile: String
        get() = pm.getString(MOBILE, "") ?: ""
        set(value) {
            pm.edit().putString(MOBILE, value).apply()
        }


}