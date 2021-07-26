package com.atdev.githubproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atdev.githubproject.model.GitHubUser

@Entity(tableName = "repository_table")
data class RepositoryEntity(

    @ColumnInfo(name = "name_cl")
    var name:String,

    @ColumnInfo(name = "owner_cl")
    var owner: String,

    @ColumnInfo(name = "avatar_url_cl")
    val avatar_url: String,

    @ColumnInfo(name = "html_url_cl")
    val html_url: String,

    @PrimaryKey
    var id: String

)
