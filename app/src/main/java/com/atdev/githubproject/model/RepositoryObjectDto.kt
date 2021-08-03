package com.atdev.githubproject.model

import java.util.*

data class RepositorySearchResult(var items: List<RepositoryObjectDto>)

data class RepositoryObjectDto(
    val name: String = "",
    val owner: GitHubUser,
    val html_url: String,
    var forks_count: String,
    var watchers_count: String,
    var language: String? = "",
    var added: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
){
    fun transformItemInDao(): RepositoryCollectionEntity {
        return RepositoryCollectionEntity(
            this.name,
            this.owner.login,
            this.owner.avatar_url,
            this.html_url,
            this.watchers_count,
            this.forks_count,
            this.language,
            this.id,
        )
    }
}


