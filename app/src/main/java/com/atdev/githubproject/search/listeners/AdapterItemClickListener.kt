package com.atdev.githubproject.search.listeners

import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.atdev.githubproject.search.model.RepositoryCollectionEntity

interface AdapterItemClickListener {
    fun onItemAddClickListener(item: RepositoryObjectDto)
}

interface AdapterDeleteItemClickListener {
    fun onItemDeleteClickListener(item: RepositoryCollectionEntity)
}