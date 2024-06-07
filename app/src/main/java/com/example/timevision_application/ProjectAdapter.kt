package com.example.timevision_application

import android.content.Intent
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class ProjectAdapter(private val projectList: List<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val projectCategory: TextView = itemView.findViewById(R.id.projectCategory)
        val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            viewDetailsButton.setOnClickListener {
                val intent = Intent(itemView.context, ProjectDetailActivity::class.java).apply {
                    putExtra("project", projectList[adapterPosition] as Serializable)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.projectName.text = currentItem.projectName
        holder.projectCategory.text = currentItem.category

        // Loading the image using Glide
        if (!currentItem.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(currentItem.imageUrl)
                .into(holder.imageView)
        } else {
            // Setting the default image if imageUrl is null or empty
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount() = projectList.size
}
