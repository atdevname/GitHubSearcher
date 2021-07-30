package com.atdev.githubproject.listeners

interface AdapterItemClickListener {
    fun onItemAddClickListener(itemID:String)
}

interface AdapterDeleteItemClickListener {
    fun onItemDeleteClickListener(itemID:String)
}