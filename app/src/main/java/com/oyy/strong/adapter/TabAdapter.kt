package com.manqian.crm.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 * Created by
 * ouyangyi on 18/5/23.
 */

class TabAdapter(fm: FragmentManager, private var fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
    internal var fm: FragmentManager? = null

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}