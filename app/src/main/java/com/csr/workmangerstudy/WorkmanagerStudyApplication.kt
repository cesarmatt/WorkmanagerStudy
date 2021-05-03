package com.csr.workmangerstudy

import android.app.Application
import com.csr.workmangerstudy.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WorkmanagerStudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WorkmanagerStudyApplication)
            modules(
                listOf(
                    networkModule,
                    homeModule,
                    createModule,
                    databaseModule,
                    workerModule
                )
            )
        }
    }
}