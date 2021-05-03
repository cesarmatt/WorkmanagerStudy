package com.csr.workmangerstudy.di

import android.content.Context
import androidx.work.WorkerParameters
import com.csr.workmangerstudy.Constants
import com.csr.workmangerstudy.DateJsonAdapter
import com.csr.workmangerstudy.create.CreateViewModel
import com.csr.workmangerstudy.data.ApiRepository
import com.csr.workmangerstudy.data.ApiService
import com.csr.workmangerstudy.data.db.AppDatabase
import com.csr.workmangerstudy.data.db.LocalDataRemoteDataSource
import com.csr.workmangerstudy.data.db.LocalDataRepository
import com.csr.workmangerstudy.home.HomeViewModel
import com.csr.workmangerstudy.workers.UploadWorker
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

val networkModule = module {
    factory {
        val httpClient = OkHttpClient.Builder().apply {
            connectTimeout(3, TimeUnit.MINUTES)
            writeTimeout(3, TimeUnit.MINUTES)
            readTimeout(3, TimeUnit.MINUTES)

            addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Connection", "Keep-Alive")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Language", Locale.getDefault().language)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }

        //val moshi = Moshi.Builder().add(Date::class.java, DateJsonAdapter()).build()
        val url = "https://6083060a5dbd2c001757b0b1.mockapi.io/"

        Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(ApiService::class.java) as ApiService }
    factory { ApiRepository(get()) }
}

val databaseModule = module {
    single(named("db")) {
        AppDatabase.buildDatabase(androidApplication())
    }
    factory { get<AppDatabase>(named("db")).postDao() }
    factory { LocalDataRemoteDataSource(get()) }
    factory { LocalDataRepository(get()) }
}

val homeModule = module {
    viewModel { HomeViewModel(get(), get(), androidApplication()) }
}

val createModule = module {
    viewModel { CreateViewModel(get()) }
}

val workerModule = module {
    worker { (context: Context, params: WorkerParameters) -> UploadWorker(context, params) }
}