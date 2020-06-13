package duodev.valerio.electric

import android.app.Application

class ValerioApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        lateinit var instance: ValerioApp
        private set
    }
}