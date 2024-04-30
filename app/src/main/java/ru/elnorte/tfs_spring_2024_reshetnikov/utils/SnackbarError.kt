package ru.elnorte.tfs_spring_2024_reshetnikov.utils

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


fun snackbarError(view: View, message: String) {
    val snack = Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    )
    val snackbarView = snack.view
    snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
    snack.show()
}
