package com.oyy.strong.http

import com.oyy.strong.BaseApplication
import com.oyy.strong.entity.GifBean
import com.oyy.strong.entity.Subtitles
import com.oyy.strong.utils.NetWorkUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by
 * ouyangyi on 18/5/22.
 */
class RetrofitManager private constructor(url: String) : Interceptor {

    //短缓存有效期为10分钟
    private val CACHE_STALE_SHORT = 60 * 10

    //长缓存有效期为7天
    private val CACHE_STALE_LONG = "60 * 60 * 24 * 7"

    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG

    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    private val CACHE_CONTROL_NETWORK = "max-age=0"

    private var mOkHttpClient: OkHttpClient? = null

    private var service: APIService? = null

    init {
        initOkHttpclient()
        var retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(APIService ::class.java)
    }

    companion object {
        //写法一： 锁在方法上
//        private var instance: RetrofitManager? = null
//
//        private @Synchronized fun getInstances( url: String): RetrofitManager {
//            if (instance == null) {
//                instance = RetrofitManager(url)
//            }
//            return instance as RetrofitManager
//        }

        fun builder(url: String):RetrofitManager {
            //双重锁
            val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitManager(url) }

            return instance
        }
    }

    /**
     * 初始化okHttp相关
     */
    private fun initOkHttpclient() {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        if (mOkHttpClient == null) {
            val cache = Cache(File(BaseApplication.getInstance().cacheDir, "File_Kotlin"), 14 * 1024 * 100)
            mOkHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .retryOnConnectionFailure(true)
                    .addNetworkInterceptor(this)
                    .addInterceptor(this)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build()
        }
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()
        if (!NetWorkUtil.isNetWorkConnected()) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }
        var response = chain.proceed(request)
        return if (NetWorkUtil.isNetWorkConnected()) {
            var cacheControl: String = request.cacheControl().toString()
            response.newBuilder().header("Cache-Control", cacheControl)
                    .removeHeader("Pragma").build()
        } else {
            response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                    .removeHeader("Pragma").build()
        }
    }

    fun getGif(subtitles: Subtitles): Observable<GifBean> = service!!.getGifPath(subtitles)

}