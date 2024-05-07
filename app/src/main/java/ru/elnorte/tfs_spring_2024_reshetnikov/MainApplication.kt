package ru.elnorte.tfs_spring_2024_reshetnikov

import android.app.Application
import ru.elnorte.tfs_spring_2024_reshetnikov.di.ApplicationGraph
import ru.elnorte.tfs_spring_2024_reshetnikov.di.DaggerApplicationGraph

class MainApplication : Application() {
    lateinit var appComponent: ApplicationGraph

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationGraph.factory().create(applicationContext)
    }
}
