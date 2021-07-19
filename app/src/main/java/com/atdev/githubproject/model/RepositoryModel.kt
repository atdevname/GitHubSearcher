package com.atdev.githubproject.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryModel(
    @SerializedName("nameRepo")
    val nameRepo: String = "",
    @SerializedName("userRepo")
    val userRepo: String = "",
) : Parcelable

data class RepositoryList(
    @SerializedName("repository")
    val cocktailList: List<RepositoryModel> = listOf()
)