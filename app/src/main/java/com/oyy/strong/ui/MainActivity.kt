package com.oyy.strong.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View.OVER_SCROLL_NEVER
import com.manqian.crm.adapter.TabAdapter
import com.manqian.crm.ui.fragment.IndexFragment
import com.manqian.crm.ui.fragment.SorryFragment
import com.manqian.crm.ui.fragment.WorkFragment
import com.oyy.strong.R
import com.oyy.strong.utils.DensityUtil
import com.oyy.strong.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast


/**
 * create by ouyangyi
 * on 18/05/22
 */

class MainActivity : AppCompatActivity() {

    private val tabs = arrayOf("wjz", "为所欲为", "打工")

    private val fragments = ArrayList<Fragment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {

        pagers.overScrollMode = OVER_SCROLL_NEVER

        fragments.add(IndexFragment())
        fragments.add(SorryFragment())
        fragments.add(WorkFragment())

        val tabSpacing = DensityUtil.getInstance().dipTopx(this, 40F)
        val cursorPadding = DensityUtil.getInstance().dipTopx(this, 8F)

        val mAdapter = TabAdapter(supportFragmentManager, fragments)
        pagers.adapter = mAdapter
        pagers.offscreenPageLimit = fragments.size

        horizontal_scrollview.setParams(tabSpacing, cursorPadding, tabs, pagers, true)

        leftText.onClick {
            toggleMenu()
        }
    }

    private fun isMenuShowing(): Boolean {
        return drawerlayout.isDrawerOpen(GravityCompat.START)
    }

    /**
     * 开关侧滑菜单
     */
    private fun toggleMenu() {
        if (isMenuShowing()) {
            drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            drawerlayout.openDrawer(GravityCompat.START)
        }
    }


    override fun onResume() {
        super.onResume()
        /**
         * 权限处理相关
         */
        PermissionUtil.requestMultiPermissions(this, arrayOf<String>(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE), mPermissionGrant)
    }

    private val mPermissionGrant = PermissionUtil.PermissionGrant {
        if (it == PermissionUtil.CODE_MULTI_PERMISSION) {

        } else {
            toast("权限被取消！将会无法正常使用！")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant)
    }
}
