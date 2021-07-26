package com.atdev.githubproject.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.setVisibility(value: Boolean) {
    (requireActivity() as? AppCompatActivity)?.setVisible(value)
}

///Для фокусировки на текст поля
//private fun getRequestField() {
//    binding.search.requestFocus()
//    val imm = requireContext().getSystemService(
//        Context.INPUT_METHOD_SERVICE
//    ) as InputMethodManager
//    imm.showSoftInput(binding.search, InputMethodManager.SHOW_IMPLICIT)
//}

//дивидер
//binding.recycler.addItemDecoration(
//DividerItemDecoration(
//requireContext(),
//DividerItemDecoration.VERTICAL
//)
//)
