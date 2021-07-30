package com.atdev.githubproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "byUser_table")
data class EntityRepositoryByUser(

    @ColumnInfo(name = "name_cl")
    var name: String,

    @ColumnInfo(name = "owner_login_cl")
    var owner_login: String,

    @ColumnInfo(name = "avatar_url_cl")
    val avatar_url: String,

    @ColumnInfo(name = "html_url_cl")
    val html_url: String,

    @ColumnInfo(name = "watchers_count_cl")
    val watchers_count: String,

    @ColumnInfo(name = "forks_count_cl")
    val forks_count: String,

    @ColumnInfo(name = "language_cl")
    val language: String? = null,

    @PrimaryKey
    var id: String,
    )
