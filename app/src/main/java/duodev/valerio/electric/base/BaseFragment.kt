package duodev.valerio.electric.base

import androidx.fragment.app.Fragment
import duodev.valerio.electric.Utils.PreferenceUtils

abstract class BaseFragment: Fragment() {
    protected val pm = PreferenceUtils
}