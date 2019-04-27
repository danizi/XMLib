package com.xm.lib.common.http

import android.app.Application
import android.text.TextUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.xm.lib.common.log.BKLog
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by xlm on 04/10/2018.
 */

class RetrofitClient {

    companion object {
        private var INSTANCE: RetrofitClient? = null
        val intance: RetrofitClient
            get() {
                if (null == INSTANCE) {
                    synchronized(RetrofitClient::class.java) {
                        if (null == INSTANCE) {
                            INSTANCE = RetrofitClient()
                        }
                    }
                }
                return INSTANCE!!
            }
    }

    private var app: Application? = null
    var retrofit: Retrofit? = null
    private var baseUrl = "https://api.caobug.com/ghost/"
    private var TIMEOUT = (5 * 1000).toLong()
    private var httpCacheDirectory = File(app?.cacheDir, "responses")
    private var cacheSize = 100 * 1024 * 1024L // 100 MiB
    var headers: HashMap<String, String>? = null

    fun setBaseUrl(url: String): RetrofitClient {
        /*设置资源访问前缀*/
        if (!TextUtils.isEmpty(url)) {
            baseUrl = url
        }
        return this
    }

    fun setTimeout(timeUnit: Long): RetrofitClient {
        /*设置读写访问时长*/
        if (timeUnit > 0) {
            TIMEOUT = timeUnit
        }
        return this
    }

    fun setHttpCacheDirectory(file: File?): RetrofitClient {
        /*设置http缓存*/
        httpCacheDirectory = if (file != null) {
            file
        } else {
            File(app?.cacheDir, "responses")
        }
        return this
    }

    fun setHeaders(headers: HashMap<String, String>): RetrofitClient {
        /*设置需要添加的请求头*/
        this.headers = headers
        return this
    }

    fun createRetrofit(app: Application): RetrofitClient {
        this.app = app
        this.retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(SecurityConverterFactory())
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return this
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                //拦截器对请求重新添加新的请求头，不加此头无法访问接口
                .addInterceptor { chain ->
                    //原始请求
                    val originalRequst = chain.request()
                    //对请求新增的一些信息
                    val requestBuilder = originalRequst.newBuilder()
                    var request: Request? = null
                    BKLog.d("请求地址：" + originalRequst.url().toString())
                    if (headers != null) {
                        for (header in headers?.entries!!) {
                            requestBuilder.addHeader(header.key, header.value)
                            BKLog.d("添加请求头：" + header.key + " ====== " + header.value)
                        }
                    }
                    //没网读取缓存
                    if (!NetworkUtil.isNetworkConnected(app?.baseContext)) {
                        requestBuilder.cacheControl(CacheControl.FORCE_CACHE)
                    }
                    //构建新的请求并返回
                    request = requestBuilder.build()
                    chain.proceed(request)
                }
                //添加网络拦截器
                .addNetworkInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (NetworkUtil.isNetworkConnected(app?.baseContext)) {
                        val maxAge = 0 * 60
                        // 有网络时 设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=$maxAge")
                                .removeHeader("Pragma")
                                .build()
                    } else {
                        // 无网络时，设置超时为1周
                        val maxStale = 60 * 60 * 24 * 7
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                                .removeHeader("Pragma")
                                .build()
                    }
                    response
                }
                .cache(okhttp3.Cache(httpCacheDirectory, cacheSize))// 100 MiB
                .build()
    }

    /**
     * 回调服务器返回其他状态码处理
     */
    abstract class BaseCallback<T> : Callback<T> {

        override fun onResponse(call: Call<T>?, response: retrofit2.Response<T>?) {
            try {
                response(call, response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onFailure(call: Call<T>?, t: Throwable) {
            try {
                if (!t.message.isNullOrEmpty()) {
                    onFailure(call, t.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun response(call: Call<T>?, response: retrofit2.Response<T>?) {
            when (response?.code()?.div(100)) {
                1 -> {
                    //消息
                }
                2 -> {
                    //请求成功
                    onSuccess(call, response)
                }
                3 -> {
                    //重定向
                }
                4 -> {
                    //客户端请求错误
                    errorMsg(response)
                    onFailure(call, response.errorBody()?.string())
                }
                5 -> {
                    //服务端处理错误
                    errorMsg(response)
                    onFailure(call, response.errorBody().toString())
                }
            }
        }

        abstract fun onSuccess(call: Call<T>?, response: retrofit2.Response<T>?)

        abstract fun onFailure(call: Call<T>?, msg: String?)

        abstract fun errorMsg(response: retrofit2.Response<T>?)
    }
}
