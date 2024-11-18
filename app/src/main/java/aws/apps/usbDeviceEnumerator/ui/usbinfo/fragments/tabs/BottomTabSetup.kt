package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.tabs

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder


class BottomTabSetup {

    fun setup(viewHolder: ViewHolder, titles: List<CharSequence>) {
        val pager = viewHolder.bottomTabViewPager
        pager.adapter = BottomTabsPagerAdapter(titles)
        viewHolder.bottomInfoTabLayout.setupWithViewPager(pager, true)
    }

    private class BottomTabsPagerAdapter(private val titles: List<CharSequence>) : PagerAdapter() {

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val resId = when (position) {
                0 -> R.id.first_bottom_table
                1 -> R.id.second_bottom_table
                else -> throw IllegalStateException("There is no tab with index $position")
            }
            return view.findViewById(resId)
        }

        override fun getCount(): Int {
            return titles.size
        }

        override fun getPageTitle(position: Int): CharSequence = titles[position]

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === (arg1 as View)
        }
    }
}