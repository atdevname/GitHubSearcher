package com.atdev.githubproject.listeners

import com.atdev.githubproject.model.RepositoryObjectDto

interface AdapterItemClickListener {
    fun onItemAddClickListener(item:RepositoryObjectDto)
}

interface AdapterDeleteItemClickListener {
    fun onItemDeleteClickListener(itemID:String)
}