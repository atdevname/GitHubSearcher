package com.atdev.githubproject.listeners

interface AdapterItemClickListener {
    fun onItemAddClickListener(item:RepositoryObjectDto)
}

interface AdapterDeleteItemClickListener {
    fun onItemDeleteClickListener(item:RepositoryDownloadedEntity)
}