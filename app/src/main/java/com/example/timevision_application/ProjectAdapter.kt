package com.example.timevision_application


import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

// Define the ProjectAdapter class which extends RecyclerView.Adapter. It takes a list of Project objects as input.
class ProjectAdapter(private val projectList: List<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    // Define the ProjectViewHolder class inside the ProjectAdapter. It holds the views for one item in the RecyclerView.
    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get the TextViews from the layout of one item
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val projectcategory: TextView = itemView.findViewById(R.id.projectCategory)
        val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)

        init {
            viewDetailsButton.setOnClickListener {
                val intent = Intent(itemView.context, ProjectDetailActivity::class.java).apply {
                    putExtra("project", projectList[adapterPosition] as Serializable)
                }
                itemView.context.startActivity(intent)
            }
        }


    }


    // Create a new ViewHolder with the layout of one item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    // Set the data of one item
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.projectName.text = currentItem.projectName
        holder.projectcategory.text = currentItem.category

    }


    // Return the size of the project list
    override fun getItemCount() = projectList.size
}