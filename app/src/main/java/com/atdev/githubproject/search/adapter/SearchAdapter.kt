package com.atdev.githubproject.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.search.listeners.AdapterItemClickListener
import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val listener: AdapterItemClickListener
) :
    PagingDataAdapter<RepositoryObjectDto, SearchAdapter.ViewHolder>(COMPARATOR) {

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
        val owner: TextView = view.findViewById(R.id.owner)
        val watchers: TextView = view.findViewById(R.id.watchers)
        var forks: TextView = view.findViewById(R.id.forks)
        var language: TextView = view.findViewById(R.id.language)

        var profileImage: ImageView = view.findViewById(R.id.profileImage)

        var add: ImageView = view.findViewById(R.id.add)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_search_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo: RepositoryObjectDto? = getItem(position)
        if (repo != null) {

            holder.name.text = repo.name
            holder.owner.text = repo.owner.login
            holder.watchers.text = repo.watchers_count
            holder.forks.text = repo.forks_count

            if (!repo.language.isNullOrEmpty()) {
                holder.language.visibility = View.VISIBLE
                holder.language.text = repo.language
            }

            Picasso.get().load(repo.owner.avatar_url).noFade().fit().centerCrop()
                .into(holder.profileImage)

            ///Переделать
            holder.add.setOnClickListener {
                listener.onItemAddClickListener(repo)
                it.setBackgroundResource(R.drawable.ic_baseline_done_24)
            }

        }
    }
}

class FooterAdapter(val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        val retryButton: Button = itemView.findViewById(R.id.retry_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_item, parent, false)
        val holder = ViewHolder(view)
        holder.retryButton.setOnClickListener {
            retry()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.retryButton.isVisible = loadState is LoadState.Error
    }
}