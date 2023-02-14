package com.atdev.githubproject.search.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.search.listeners.AdapterItemClickListener
import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class SearchAdapter(
    private val listener: AdapterItemClickListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var dataSet: List<RepositoryObjectDto> by Delegates.observable(listOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val owner: TextView = view.findViewById(R.id.owner)
        val name: TextView = view.findViewById(R.id.name)
        val watchers: TextView = view.findViewById(R.id.watchers)
        var forks: TextView = view.findViewById(R.id.forks)
        var language: TextView = view.findViewById(R.id.language)
        var profileImage: ImageView = view.findViewById(R.id.profileImage)
        val add: ImageView = view.findViewById(R.id.add)

        init {
            add.isVisible = true

            itemView.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[adapterPosition].html_url))
                view.context.startActivity(intent)
            }

            add.setOnClickListener {
                it.setBackgroundResource(R.drawable.ic_baseline_done_24)
                listener.onItemAddClickListener(dataSet[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_repository, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Picasso.get().load(dataSet[position].owner.avatar_url).noFade().fit().centerCrop()
            .into(viewHolder.profileImage)

        viewHolder.owner.text = dataSet[position].owner.login
        viewHolder.name.text = dataSet[position].name

        viewHolder.watchers.text = dataSet[position].watchers_count
        viewHolder.forks.text = dataSet[position].forks_count

        if (dataSet[position].language.isNullOrEmpty()) {
            viewHolder.language.visibility = View.VISIBLE
            viewHolder.language.text = dataSet[position].language
        }
    }

    override fun getItemCount() = dataSet.size
}