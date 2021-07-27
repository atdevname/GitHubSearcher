package com.atdev.githubproject.retrofit

import android.content.Context
import com.atdev.githubproject.di.CommonModule.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitAPIClient {
    private var retrofit: Retrofit? = null
    fun getRetrofitClient(mContext: Context): Retrofit? {
        if (retrofit == null) {
            val oktHttpClient = OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(mContext))
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build()
        }
        return retrofit
    }
}