package com.pingpong_android.network

import android.content.Context
import com.pingpong_android.base.Constants.Companion.BASE_URL
import com.pingpong_android.utils.PreferenceUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient() {
    companion object{
        private var retrofitClient: RetrofitService? = null
        private var Base_Url = BASE_URL
        private var Flag = false

        private val okHttpClient: OkHttpClient by lazy {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .connectTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }

        fun getClient(isLogin : Boolean): RetrofitService?{
            if (isLogin) {
                retrofitClient = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)  // provideOkHttpClient(AppInterceptor())
                    .build().create(RetrofitService::class.java)

            } else if (Flag || retrofitClient == null) {
                retrofitClient = Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)  // provideOkHttpClient(AppInterceptor())
                    .build().create(RetrofitService::class.java)

                setFlag(false)
            }
            return retrofitClient
        }

        private fun setFlag(flag : Boolean) {
            Flag = flag
        }
    }
}