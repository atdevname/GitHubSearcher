package com.atdev.githubproject.di

import android.app.Application
import android.content.Context
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.retrofit.ApiService
import com.atdev.githubproject.retrofit.NetworkConnectionInterceptor
import com.atdev.githubproject.room.RepositoryDao
import com.atdev.githubproject.room.RepositoryDatabase
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
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
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
    fun provideDatabase(application: Application) = RepositoryDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun provideDao(db: RepositoryDatabase) = db.repositoryDao()

    @Singleton
    @Provides
    fun provideRepository(
        repositoryDao: RepositoryDao,
        apiService: ApiService
    ) = MainRepository(repositoryDao, apiService)

}