package com.atdev.githubproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.model.RepositoryObjectDto


class MainListAdapter : PagingDataAdapter<RepositoryObjectDto, MainListAdapter.ViewHolder>(DataDifferntiator) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.name)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name = "${getItem(position)?.name} ${getItem(position)?.language}"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_search_repository, parent, false)
        )
    }

    object DataDifferntiator : DiffUtil.ItemCallback<RepositoryObjectDto>() {

        override fun areItemsTheSame(oldItem: RepositoryObjectDto, newItem: RepositoryObjectDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepositoryObjectDto, newItem: RepositoryObjectDto): Boolean {
            return oldItem == newItem
        }
    }

}
