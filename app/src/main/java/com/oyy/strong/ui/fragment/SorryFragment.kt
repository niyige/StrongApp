package com.manqian.crm.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oyy.strong.R
import com.oyy.strong.entity.Subtitles
import com.oyy.strong.http.RetrofitManager
import com.oyy.strong.ui.fragment.BaseV4Fragment
import com.oyy.strong.utils.*
import kotlinx.android.synthetic.main.fragment_sorry_layout.*
import org.jetbrains.anko.onClick
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*

/**
 * sorry fragment
 * Created by
 * ouyangyi on 18/5/23.
 */

class SorryFragment : BaseV4Fragment() {

    private var path = Constant.BASE_PATH + "/sorry_douTu.gif"

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    cancelWaitingDialog()
                    Glide.with(activity)
                            .load(path)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(showSorryGif)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = null
        if (inflater != null) {
            view = inflater.inflate(R.layout.fragment_sorry_layout, null)

        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initClick()
    }

    private fun initView() {
        //去除阴影
        Utils.getInstance().setOverScrollMode(scrollView)
    }

    /**
     * 初始化监听事件
     */
    private fun initClick() {

        submitBtn.onClick {
            var a1 = edit_1.text
            var a2 = edit_2.text
            var a3 = edit_3.text
            var a4 = edit_4.text
            var a5 = edit_5.text
            var a6 = edit_6.text
            var a7 = edit_7.text
            var a8 = edit_8.text
            var a9 = edit_9.text

            if (TextUtils.isEmpty(a1) || TextUtils.isEmpty(a2)
                    || TextUtils.isEmpty(a3) || TextUtils.isEmpty(a4) ||
                    TextUtils.isEmpty(a5) || TextUtils.isEmpty(a6) ||
                    TextUtils.isEmpty(a7) || TextUtils.isEmpty(a8) || TextUtils.isEmpty(a9)) {

                Toast.makeText(activity, "请输入完整", Toast.LENGTH_SHORT)
            } else {
                var sentence = "$a1,$a2,$a3,$a4,$a5,$a6,$a7,$a8,$a9"
                var subtitles = Subtitles("sorry", sentence, "simple")
                RetrofitManager.builder("http://193.112.131.84:8081")
                        .getGif(subtitles)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            showWaitingDialog()
                            println("call………………………执行前调用…")
                        }
                        .subscribe({ ban ->
                            Thread {
                                var type = "sorry" + Date().time
                                path = Constant.BASE_PATH + "/" + type + "_douTu.gif"
                                DataCleanManagerUtils.cleanApplicationData(mContext, Constant.BASE_PATH)
                                ImageHandlerUtils.downLoadImgSaveFile(ban.result, type)
                                val message = Message.obtain()
                                message.what = 1
                                handler.sendMessage(message)
                            }.start()

                        })
                        { throwable ->
                            println(throwable.message)
                        }
            }

        }

        shareBtn.onClick {
            ShareUtils.shareFile(activity, File(path))
        }
    }
}