package com.atdev.githubproject.listeners

import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.room.RepositoryCollectionEntity

interface AdapterItemClickListener {
    fun onItemAddClickListener(item: RepositoryObjectDto)
}

interface AdapterDeleteItemClickListener {
    fun onItemDeleteClickListener(item: RepositoryCollectionEntity)
}