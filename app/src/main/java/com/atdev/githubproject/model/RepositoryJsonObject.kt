package com.atdev.githubproject.model

data class RepositoryJsonObject(
    val owner: GitHubUser,
    val name: String = ""
)
