package ru.elnorte.tfs_spring_2024_reshetnikov

import android.app.Application
import ru.elnorte.tfs_spring_2024_reshetnikov.di.DaggerApplicationGraph

class MainApplication : Application() {
    val appComponent = DaggerApplicationGraph.create()
}
