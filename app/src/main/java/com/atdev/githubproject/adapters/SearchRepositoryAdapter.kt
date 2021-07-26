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
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class SearchRepositoryAdapter(
    private val listener: AdapterItemClickListener
) :
    RecyclerView.Adapter<SearchRepositoryAdapter.ViewHolder>() {

    var dataSet: List<RepositoryJsonObject> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        val owner: TextView = view.findViewById(R.id.owner)
        val name: TextView = view.findViewById(R.id.name)

        var profileImage: ImageView = view.findViewById(R.id.profileImage)

        var add: ImageView = view.findViewById(R.id.add)

        init {

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[adapterPosition].html_url))
                view.context.startActivity(intent)
            }

            //ну а вдруг что-то пойдет не так и айтем не добавится? как правильно позаботиться чтобы в этом случае дровабл не сменился
            add.setOnClickListener {
                listener.onItemAddClickListener(dataSet[adapterPosition].id)
                dataSet[adapterPosition].added = true
                add.setBackgroundResource(R.drawable.ic_baseline_done_24)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_repository_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.owner.text = dataSet[position].owner.login
        viewHolder.name.text = dataSet[position].name

        Picasso.get().load(dataSet[position].owner.avatar_url).noFade().fit().into(viewHolder.profileImage)

        if (dataSet[position].added) {
            viewHolder.add.setBackgroundResource(R.drawable.ic_baseline_done_24)
        }else{
            viewHolder.add.setBackgroundResource(R.drawable.ic_baseline_add_24)
        }

    }

    override fun getItemCount() = dataSet.size

}
