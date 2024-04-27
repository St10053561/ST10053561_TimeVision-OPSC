package com.example.timevision_application

import Project
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

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

        val projectList = DumyData()

// Get the RecyclerView from the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

// Set the adapter for the RecyclerView to a new ProjectAdapter, which will handle displaying the projects
        recyclerView.adapter = ProjectAdapter(projectList)

// Set the layout manager for the RecyclerView to a LinearLayoutManager, which will arrange the items in a vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

// Set the RecyclerView to have a fixed size for performance optimization, as the size of the RecyclerView doesn't change with its content
        recyclerView.setHasFixedSize(true)

    }

    private fun DumyData(): List<Project> {
        val projectList = listOf(
            Project(
                "Project Alpha",
                "Software Development",
                "This is a software development project",
                2,
                8,
                "Photo 1",
                "2023-01-01",
                List(7) { Random.nextInt(2 ,8) }
            ),
            Project(
                "Project Beta",
                "Marketing",
                "This is a marketing project",
                3,
                10,
                "Photo 2",
                "2023-02-01",
                List(7) { Random.nextInt(3 ,10) }
            ),
            Project(
                "Project Gamma",
                "Product Development",
                "This is a product development project",
                20,
                40,
                "Photo 3",
                "2023-03-01",
                List(7) { Random.nextInt(3 ,10) }
            ),
            Project(
                "Project Delta",
                "Human Resource",
                "This is a human resource project",
                25,
                50,
                "Photo 4",
                "2023-04-01",
                List(7) { Random.nextInt(3 ,10) }
            ),
            Project(
                "Project Epsilon",
                "Operations",
                "This is an operations project",
                30,
                60,
                "Photo 5",
                "2023-05-01",
                List(7) { Random.nextInt(3 ,10) }
            )
        )
        return projectList
    }
}