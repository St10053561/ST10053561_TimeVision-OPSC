package com.example.timevision_application

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Hide the status bar.
        supportActionBar?.hide()

        // Hide both the navigation bar and the status bar.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // Create a list of Project objects. Each Project has a name and a category.
        val projectList = listOf(
            Project("Project 1", "Category 1"),
            Project("Project 2", "Category 2"),
            Project("Project 3", "Category 3"),
            Project("Project 4", "Category 4"),
            Project("Project 5", "Category 5"),
            Project("Project 6", "Category 6")

        )

// Get the RecyclerView from the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

// Set the adapter for the RecyclerView to a new ProjectAdapter, which will handle displaying the projects
        recyclerView.adapter = ProjectAdapter(projectList)

// Set the layout manager for the RecyclerView to a LinearLayoutManager, which will arrange the items in a vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

// Set the RecyclerView to have a fixed size for performance optimization, as the size of the RecyclerView doesn't change with its content
        recyclerView.setHasFixedSize(true)


    }
}