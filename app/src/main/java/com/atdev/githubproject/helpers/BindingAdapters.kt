package com.atdev.githubproject.helpers

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

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


