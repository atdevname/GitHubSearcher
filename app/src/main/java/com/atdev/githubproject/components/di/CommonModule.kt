package com.atdev.githubproject.components.di

import android.app.Application
import android.content.Context
import com.atdev.githubproject.collection.room.AppDatabase
import com.atdev.githubproject.search.api.ApiService
import com.atdev.githubproject.search.api.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CommonModule {

    @Singleton
    @Provides
    fun provideRetrofitService(@ApplicationContext context: Context): ApiService {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(
                GsonConverterFactory.create()
            ).client(okHttpClient).build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = AppDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.repositoryDao()
}