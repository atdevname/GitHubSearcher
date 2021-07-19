package com.atdev.githubproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.room.RepositoryEntity
import kotlin.properties.Delegates

class DownloadedListAdapter() :
    RecyclerView.Adapter<DownloadedListAdapter.ViewHolder>() {

    private var dataSet: List<RepositoryEntity> by Delegates.observable(ArrayList()){ _, _, _ ->
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val owner: TextView = view.findViewById(R.id.owner)
        val name: TextView = view.findViewById(R.id.name)

        var download:ImageView = view.findViewById(R.id.download)
        var delete:ImageView = view.findViewById(R.id.download)

        init {
            itemView.setOnClickListener {
            }

            download.setOnClickListener {
            }

            delete.setOnClickListener {
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_repository_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.owner.text = dataSet[position].owner
        viewHolder.name.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size

}
