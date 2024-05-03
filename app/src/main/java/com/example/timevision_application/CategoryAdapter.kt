package com.example.timevision_application

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.categoryName)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name

        holder.editButton.setOnClickListener {
            // Create an EditText
            val input = EditText(holder.view.context)
            // Create an AlertDialog
            val dialog = AlertDialog.Builder(holder.view.context)
                .setTitle("Edit Category")
                .setMessage("Enter new category name:")
                .setView(input)
                .setPositiveButton("OK") { dialog, _ ->
                    // Get the input from the EditText
                    val newName = input.text.toString()
                    // Update the category name
                    category.name = newName
                    // Notify the adapter that the item has changed
                    notifyItemChanged(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
            // Show the AlertDialog
            dialog.show()
        }

        holder.deleteButton.setOnClickListener {
            // Handle delete action
            categories.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = categories.size
}