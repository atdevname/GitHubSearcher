package com.atdev.githubproject.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atdev.githubproject.R
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.room.RepositoryEntity
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class DownloadedListAdapter(
    private val listener: AdapterItemClickListener
) :
    RecyclerView.Adapter<DownloadedListAdapter.ViewHolder>() {

    var dataSet: List<RepositoryEntity> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val owner: TextView = view.findViewById(R.id.owner)
        val name: TextView = view.findViewById(R.id.name)

        var profileImage: ImageView = view.findViewById(R.id.profileImage)

        private var delete: ImageView = view.findViewById(R.id.delete)

        init {
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[adapterPosition].html_url))
                view.context.startActivity(intent)
            }

            delete.setOnClickListener {
                listener.onItemDeleteClickListener(dataSet[adapterPosition].id)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.downloaded_repository_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.owner.text = dataSet[position].owner
        viewHolder.name.text = dataSet[position].name

        Picasso.get().load(dataSet[position].avatar_url).noFade().fit().into(viewHolder.profileImage)

    }

    override fun getItemCount() = dataSet.size

}
