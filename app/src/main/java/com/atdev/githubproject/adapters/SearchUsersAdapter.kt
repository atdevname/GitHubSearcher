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
import com.atdev.githubproject.model.RepositoryJsonObject
import kotlin.properties.Delegates

class SearchUsersAdapter(
    private val listener: AdapterItemClickListener
) :
    RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>() {

    var dataSet: List<RepositoryJsonObject> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.name)
        var add: ImageView = view.findViewById(R.id.add)
        val watchers:TextView = view.findViewById(R.id.watchers)
        var forks:TextView = view.findViewById(R.id.forks)
        var language:TextView = view.findViewById(R.id.language)

        init {

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[absoluteAdapterPosition].html_url))
                view.context.startActivity(intent)
            }

            //ну а вдруг что-то пойдет не так и айтем не добавится? как правильно позаботиться чтобы в этом случае дровабл не сменился
            add.setOnClickListener {
                listener.onItemAddClickListener(dataSet[absoluteAdapterPosition].id)
                dataSet[absoluteAdapterPosition].added = true
                add.setBackgroundResource(R.drawable.ic_baseline_done_24)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_search_users, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet[position].name
        viewHolder.watchers.text = dataSet[position].watchers_count
        viewHolder.forks.text = dataSet[position].forks_count

        if (dataSet[position].language != null){
            viewHolder.language.visibility = View.VISIBLE
            viewHolder.language.text = dataSet[position].language
        }

        if (dataSet[position].added) {
            viewHolder.add.setBackgroundResource(R.drawable.ic_baseline_done_24)
        }else{
            viewHolder.add.setBackgroundResource(R.drawable.ic_baseline_add_24)
        }
    }

    override fun getItemCount() = dataSet.size

}
