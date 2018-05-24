package com.oyy.strong.utils

import com.oyy.strong.BaseApplication
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by
 * ouyangyi on 18/4/26.
 */
class NetWorkUtil {
    companion object {
        fun isNetWorkConnected(): Boolean {
            val cm = BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val currentNet = cm.activeNetworkInfo ?: return false
            return currentNet.isAvailable
        }
    }
}
