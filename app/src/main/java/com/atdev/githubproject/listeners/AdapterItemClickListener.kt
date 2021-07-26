package com.atdev.githubproject.listeners

import com.atdev.githubproject.model.RepositoryJsonObject

interface AdapterItemClickListener {
    fun onItemDownloadClickListener(item:RepositoryJsonObject)
    fun onItemAddClickListener(itemID:String)
    fun onItemDeleteClickListener(itemID:String)

}