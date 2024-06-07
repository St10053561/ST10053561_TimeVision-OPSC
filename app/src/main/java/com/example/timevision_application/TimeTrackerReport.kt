package com.example.timevision_application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class TimeTrackerReport : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_timesheet_v)

        // Hide the status bar.
        FullScreenMode()

        // Get the RecyclerView from the layout
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set the layout manager for the RecyclerView to a LinearLayoutManager, which will arrange the items in a vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the RecyclerView to have a fixed size for performance optimization, as the size of the RecyclerView doesn't change with its content
        recyclerView.setHasFixedSize(true)

        DumyData { projectList ->
            // Set the adapter for the RecyclerView to a new ProjectAdapter, which will handle displaying the projects
            recyclerView.adapter = ProjectAdapter(projectList)
        }

        // Find the button in the layout
        val openDatePickerButton: Button = findViewById(R.id.openDatePickerButton)

        // Do something when the button is clicked
        openDatePickerButton.setOnClickListener {
            // Create an intent to open the DatePickerActivity
            val intent = Intent(this, DatePickerActivity::class.java)
            // Start the DatePickerActivity and wait for it to give us a result
            startActivityForResult(intent, DATE_PICKER_REQUEST)
        }

        // Find the backButton button by its ID
        val backButton: Button = findViewById(R.id.backButton)
        // Set a click listener for the backButton button
        backButton.setOnClickListener {
            onBackPressed()
        }
    }


    private fun FullScreenMode() {
        // Hide the status bar.
        supportActionBar?.hide()

        // Hide both the navigation bar and the status bar.
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
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


    private fun DumyData(callback: (List<Project>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("TimeSheetEntries")
        val projectList = mutableListOf<Project>()

        // Get the current user ID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) { // Iterate over each user
                    // Check if the user ID matches the current user ID
                    if (userSnapshot.key == currentUserId) {
                        for (projectSnapshot in userSnapshot.children) { // Iterate over each project of the user
                            val project = projectSnapshot.getValue(Project::class.java)
                            if (project != null) {
                                projectList.add(project)
                            }
                        }
                    }
                }
                callback(projectList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }


    private fun filterProjects(startDate: Long, endDate: Long) {
        DumyData { projectList ->
            val filteredProjects = projectList.filter { project ->
                // Convert the project date to a format we can compare
                val projectDate = SimpleDateFormat("M/d/yyyy", Locale.getDefault()).parse(project.date).time
                // Check if the project date is between the start and end dates
                projectDate in startDate..endDate
            }
            // Update the RecyclerView to show the filtered projects
            recyclerView.adapter = ProjectAdapter(filteredProjects)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Start the HomeScreen activity when the backButton is pressed
        startActivity(Intent(this, HomeScreen::class.java))
        // Finish the current activity
        finish()
    }

    companion object {

        const val DATE_PICKER_REQUEST = 1
    }
}