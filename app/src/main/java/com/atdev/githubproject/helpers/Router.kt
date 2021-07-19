package com.atdev.githubproject.helpers

import androidx.navigation.NavController
import com.atdev.githubproject.R


class Router constructor(private val navController: NavController) {

    fun toDownloadedFragment() : Boolean {
        navController.navigate(R.id.toDownloadFragment)
        return true
    }

}