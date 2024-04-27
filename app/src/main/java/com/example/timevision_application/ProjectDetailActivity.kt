package com.example.timevision_application

import Project
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProjectDetailActivity :
    AppCompatActivity() { // Define a new activity called ProjectDetailActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call the superclass onCreate() method
        setContentView(R.layout.activity_project_detail) // Set the layout for this activity

        //Find the views by their IDs
        val projectName: TextView = findViewById(R.id.projectName)
        val projectCategory: TextView = findViewById(R.id.projectCategory)
        val description: TextView = findViewById(R.id.description)
        val hours: TextView = findViewById(R.id.hours)
        val userPhoto: TextView = findViewById(R.id.userPhoto)
        val projectDate: TextView = findViewById(R.id.projectDate)



        // Retrieve the project object from the previous activity
        val project = intent.getSerializableExtra("project") as? Project

        if (project != null) {
            projectName.text = project.projectname
            projectCategory.text = project.category
            description.text = project.description
            hours.text = "${project.minHours} - ${project.maxHours}"
            userPhoto.text = project.userPhoto
            projectDate.text = project.projectDate
        }
    }
}