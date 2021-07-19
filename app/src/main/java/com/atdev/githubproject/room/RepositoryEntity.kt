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

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
