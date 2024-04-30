package com.example.timevision_application

import Project
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Hide the status bar.
        supportActionBar?.hide()

        // Hide both the navigation bar and the status bar.
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val projectList = DumyData()

// Get the RecyclerView from the layout
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

// Set the adapter for the RecyclerView to a new ProjectAdapter, which will handle displaying the projects
        recyclerView.adapter = ProjectAdapter(projectList)

// Set the layout manager for the RecyclerView to a LinearLayoutManager, which will arrange the items in a vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

// Set the RecyclerView to have a fixed size for performance optimization, as the size of the RecyclerView doesn't change with its content
        recyclerView.setHasFixedSize(true)


        // Find the button in the layout
        val openDatePickerButton: Button = findViewById(R.id.openDatePickerButton)

// Do something when the button is clicked
        openDatePickerButton.setOnClickListener {
            // Create an intent to open the DatePickerActivity
            val intent = Intent(this, DatePickerActivity::class.java)
            // Start the DatePickerActivity and wait for it to give us a result
            startActivityForResult(intent, DATE_PICKER_REQUEST)
        }


    }

    // This function is called when another activity gives us a result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Do what we normally do when we get a result
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result comes from the DatePickerActivity and if it was successful
        if (requestCode == DATE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            // Get the start and end dates from the result
            val startDate = data?.getLongExtra("startDate", 0)
            val endDate = data?.getLongExtra("endDate", 0)

            // If we got both dates, filter the projects
            if (startDate != null && endDate != null) {
                filterProjects(startDate, endDate)
            }
        }
    }

    // This function filters projects based on start and end dates
    private fun filterProjects(startDate: Long, endDate: Long) {
        val filteredProjects = DumyData().filter { project ->
            // Convert the project date to a format we can compare
            val projectDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(project.projectDate).time
            // Check if the project date is between the start and end dates
            projectDate in startDate..endDate
        }
        // Update the RecyclerView to show the filtered projects
        recyclerView.adapter = ProjectAdapter(filteredProjects)
    }

    private fun DumyData(): List<Project> {
        val projectList = listOf(
            Project("Project Alpha",
                "Software Development",
                "This is a software development project",
                2,
                8,
                R.drawable.person,
                "2024-04-01",
                List(7) { Random.nextInt(2, 8) }), Project("Project Beta",
                "Marketing",
                "This is a marketing project",
                3,
                10,
                R.drawable.person,
                "2024-04-05",
                List(7) { Random.nextInt(3, 10) }), Project("Project Gamma",
                "Product Development",
                "This is a product development project",
                20,
                40,
                R.drawable.person,
                "2024-04-20",
                List(7) { Random.nextInt(3, 10) }), Project("Project Delta",
                "Human Resource",
                "This is a human resource project",
                25,
                50,
                R.drawable.person,
                "2024-04-30",
                List(7) { Random.nextInt(3, 10) }), Project("Project Epsilon",
                "Operations",
                "This is an operations project",
                30,
                60,
                R.drawable.person,
                "2024-05-01",
                List(7) { Random.nextInt(3, 10) })
        )
        return projectList
    }

    companion object {
        const val DATE_PICKER_REQUEST = 1
    }
}