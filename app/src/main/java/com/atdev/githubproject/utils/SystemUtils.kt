package com.atdev.githubproject.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Fragment.setVisibility(value: Boolean) {
    (requireActivity() as? AppCompatActivity)?.setVisible(value)
}

