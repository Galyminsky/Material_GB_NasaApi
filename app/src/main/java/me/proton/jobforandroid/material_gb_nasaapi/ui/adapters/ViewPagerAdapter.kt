package me.proton.jobforandroid.material_gb_nasaapi.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import me.proton.jobforandroid.material_gb_nasaapi.ui.earth_fragment.EarthFragment
import me.proton.jobforandroid.material_gb_nasaapi.ui.pod_fragment.PODFragment

private const val POD_FRAGMENT = 0
private const val EARTH_FRAGMENT = 1
private const val MARS_FRAGMENT = 2
private const val MOON_FRAGMENT = 3

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(PODFragment(), EarthFragment())

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[POD_FRAGMENT]
            1 -> fragments[EARTH_FRAGMENT]
            else -> fragments[POD_FRAGMENT]
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "POD"
            1 -> "Earth"
            else -> "POD"
        }
    }
}
