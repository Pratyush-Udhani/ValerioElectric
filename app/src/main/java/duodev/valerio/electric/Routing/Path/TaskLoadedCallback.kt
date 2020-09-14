package duodev.valerio.electric.Routing.Path

import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

interface TaskLoadedCallback {
    fun onTaskDone(value: ArrayList<LatLng?>)
}