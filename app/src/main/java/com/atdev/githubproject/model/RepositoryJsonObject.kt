package com.atdev.githubproject.model

import java.util.*

data class RepositoryJsonObject(
    val id: String = UUID.randomUUID().toString(),
    val owner: GitHubUser,
    val name: String = "",
    val html_url: String,
    var forks_count:String,
    var watchers_count:String,
    var language: String? = null,
    var added:Boolean = false
)
