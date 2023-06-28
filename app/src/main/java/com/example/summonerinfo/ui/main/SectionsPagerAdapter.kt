package com.example.summonerinfo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.summonerinfo.R
import com.example.summonerinfo.listeners.DataUpdateListener

private val TAB_TITLES = arrayOf(
    R.string.Rotation,
    R.string.ChampionsMastery,
    R.string.RandomChampion
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        val mainActivity = context as DataUpdateListener

        //return PlaceholderFragment.newInstance(position + 1, mainActivity)
       /* return when (position) {
            0 -> RotationFragment(mainActivity)           // Pierwsza zakładka - fragment rotacji
            1 -> ChampionsFragment(mainActivity)           // Druga zakładka - fragment danych bohaterów
            2 -> RandomChampionFragment(mainActivity)     // Trzecia zakładka - fragment losowego bohatera
            else -> throw IllegalArgumentException("Invalid section position")
        }*/
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return context.resources.getString(TAB_TITLES[position])
        return when (position) {
            0 -> context.resources.getString(TAB_TITLES[0])        // Tytuł dla pierwszej zakładki
            1 -> context.resources.getString(TAB_TITLES[1])       // Tytuł dla drugiej zakładki
            2 -> context.resources.getString(TAB_TITLES[2]) // Tytuł dla trzeciej zakładki
            else -> null
        }
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }

    fun addFragment(fragment: Fragment) {
        // add each fragment and its title to the array list
        mFragmentList.add(fragment)
    }
}