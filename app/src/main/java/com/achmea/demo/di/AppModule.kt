package com.achmea.demo.di

import android.content.Context
import androidx.room.Room
import com.achmea.demo.common.Constants
import com.achmea.demo.data.local.AppDatabase
import com.achmea.demo.data.local.EmployerDao
import com.achmea.demo.data.remote.EmployerApi
import com.achmea.demo.data.repository.EmployerRepositoryImpl
import com.achmea.demo.domain.repository.EmployerRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


interface AppModule {
    val employerRepository: EmployerRepository
    val employerApi: EmployerApi
    val appDatabase: AppDatabase
    val employerDao: EmployerDao
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {

    override val employerApi: EmployerApi by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(EmployerApi::class.java)
    }


    override val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "acheam-demo").build()
    }
    override val employerDao: EmployerDao by lazy {
        appDatabase.employerDao
    }

    override val employerRepository: EmployerRepository by lazy {
        EmployerRepositoryImpl(employerApi, employerDao)
    }
}