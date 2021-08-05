package com.atdev.githubproject.components.utils

import android.view.View
import android.widget.*
import androidx.databinding.*

@BindingMethods(
    value = [BindingMethod(
        type = TextView::class,
        attribute = "android:textColorRes",
        method = "setTextColorResource"
    )]
)
class BindingAdapters

@BindingAdapter(value = ["android:visibility"])
fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}


