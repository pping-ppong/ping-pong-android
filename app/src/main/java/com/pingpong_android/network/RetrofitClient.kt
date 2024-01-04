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

        fun getClient(isLogin : Boolean): RetrofitService?{
            if (isLogin) {
                retrofitClient = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(provideOkHttpClient(AppInterceptor()))
                    .build().create(RetrofitService::class.java)

            } else if (Flag || retrofitClient == null) {
                retrofitClient = Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(provideOkHttpClient(AppInterceptor()))
                    .build().create(RetrofitService::class.java)

                setFlag(false)
            }
            return retrofitClient
        }

        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
                = OkHttpClient.Builder().run {
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .build()
                proceed(newRequest)
            }
        }

        private fun setFlag(flag : Boolean) {
            Flag = flag
        }
    }
}