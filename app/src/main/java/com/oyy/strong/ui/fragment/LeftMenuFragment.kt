package com.oyy.strong.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oyy.strong.R
import com.oyy.strong.ui.AboutActivity
import com.oyy.strong.utils.Constant
import com.oyy.strong.utils.DataCleanManagerUtils
import kotlinx.android.synthetic.main.fragment_left_menu.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 侧边栏
 * Created by
 * ouyangyi on 18/5/3.
 */

class LeftMenuFragment: BaseV4Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = null
        if(inflater != null) {
            view = inflater.inflate(R.layout.fragment_left_menu, null)

        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initClick()
    }

    private fun initClick() {

        clearTxt.onClick {
            showWaitingDialog()
            DataCleanManagerUtils.cleanApplicationData(mContext, Constant.BASE_PATH)
            cancelWaitingDialog()
            activity.toast("清理完成")
        }

        aboutTxt.onClick {
            activity.startActivity<AboutActivity>()
        }

    }
}