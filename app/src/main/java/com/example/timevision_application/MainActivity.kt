package com.example.timevision_application

import Project
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        //initialize the table layout values
        val projects = listOf(
            Project("Project 1", "Category 1", "01/01/2021", "Status 1", "StartEnd 1", 1.0f, 1, 1, "Photo 1"),
            Project("Project 2", "Category 2", "02/02/2022", "Status 2", "StartEnd 2", 2.0f, 2, 2, "Photo 2"),
            Project("Project 3", "Category 3", "03/03/2023", "Status 3", "StartEnd 3", 3.0f, 3, 3, "Photo 3")
        )

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val tableManager = TableManager(this, tableLayout)
        tableManager.populateTable(projects)


    }
}