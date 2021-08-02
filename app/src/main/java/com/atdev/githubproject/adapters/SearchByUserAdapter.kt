package com.atdev.githubproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto

class SearchByUserAdapter(
    private val listener: AdapterItemClickListener
) :
    PagingDataAdapter<RepositoryObjectDto, SearchByUserAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RepositoryObjectDto>() {
            override fun areItemsTheSame(
                oldItem: RepositoryObjectDto,
                newItem: RepositoryObjectDto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RepositoryObjectDto,
                newItem: RepositoryObjectDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val watchers: TextView = view.findViewById(R.id.watchers)
        var forks: TextView = view.findViewById(R.id.forks)
        var language: TextView = view.findViewById(R.id.language)

        var add: ImageView = view.findViewById(R.id.addRepo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_search_users, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        if (repo != null) {

            holder.name.text = repo.name

            holder.name.text = repo.name

            holder.watchers.text = repo.watchers_count
            holder.forks.text = repo.forks_count

            if (repo.language!!.isNotEmpty()) {
                holder.language.visibility = View.VISIBLE
                holder.language.text = repo.language
            }

            ///Переделать
            holder.add.setOnClickListener {
                listener.onItemAddClickListener(repo)
                it.setBackgroundResource(R.drawable.ic_baseline_done_24)

            }

        }
    }
}
