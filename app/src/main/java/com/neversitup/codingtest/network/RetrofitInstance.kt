package com.neversitup.codingtest.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.neversitup.codingtest.application.MyApplication
import com.neversitup.codingtest.utility.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object RetrofitInstance {

        private val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(MyApplication.instance))
                .build()

        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }

        val api : Api by lazy {
            retrofit.create(Api::class.java)
        }
    }

}