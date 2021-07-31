package com.atdev.githubproject.model

import java.util.*

data class RepositoryObjectDto(
    val name: String = "",
    val owner: GitHubUser,
    val html_url: String,
    var forks_count: String,
    var watchers_count: String,
    var language: String? = "",
    var added: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
)
