package ru.elnorte.tfs_spring_2024_reshetnikov.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun snackbarError(view: View, t: Throwable) {
    Snackbar.make(
        view,
        "Something went wrong ${t.message.toString()}",
        Snackbar.LENGTH_SHORT
    ).show()
}
