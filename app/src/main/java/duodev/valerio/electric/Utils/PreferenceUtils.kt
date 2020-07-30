package duodev.valerio.electric.Utils

import android.preference.PreferenceManager
import duodev.valerio.electric.ValerioApp
import duodev.valerio.electric.pojos.Users

object PreferenceUtils {

    private val pm = PreferenceManager.getDefaultSharedPreferences(ValerioApp.instance)

    private const val NAME = "name"
    private const val EMAIL = "email"
    private const val MOBILE = "mobile"
    private const val ADDRESS = "address"
    private const val ACCOUNT = "account"
    private const val HASH = "hash"

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

    var account: Boolean
        get() = pm.getBoolean(ACCOUNT, false)
        set(value) {
            pm.edit().putBoolean(ACCOUNT, value).apply()
        }

    var mobile: String
        get() = pm.getString(MOBILE, "") ?: ""
        set(value) {
            pm.edit().putString(MOBILE, value).apply()
        }

    var address: String
        get() = pm.getString(ADDRESS, "") ?: ""
        set(value) {
            pm.edit().putString(ADDRESS, value).apply()
        }

    var hash: String
        get() = pm.getString(HASH, "") ?: ""
        set(value) {
            pm.edit().putString(HASH, value).apply()
        }


    fun getUser(): Users {
        return Users()
    }

    fun setUser(users: Users) {
        val pm = PreferenceUtils
        pm.name = users.name?.trimString()!!
        pm.email = users.email?.trimString()!!
        pm.mobile = users.contact?.trimString()!!
        pm.hash = users.hash?.trimString()!!
        pm.address = users.address?.trimString()!!
    }

    fun resetUser() {
        val pm = PreferenceUtils
        pm.hash = ""
        pm.name = ""
        pm.email = ""
        pm.mobile = ""
        pm.address = ""
        pm.account = false
    }

}