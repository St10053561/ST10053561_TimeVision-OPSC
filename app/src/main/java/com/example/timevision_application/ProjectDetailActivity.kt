package com.example.timevision_application

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChartView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("NAME_SHADOWING")
class ProjectDetailActivity : AppCompatActivity() {

    // Declare hoursMap as a member variable
    private val hoursMap = HashMap<String, MutableList<Int>>()

    // Declare currentMonthOffset as a member variable
    private var currentMonthOffset = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)

        val projectName: TextView = findViewById(R.id.projectName)
        val projectCategory: TextView = findViewById(R.id.projectCategory)
        val description: TextView = findViewById(R.id.description)
        val hours: TextView = findViewById(R.id.hours)
        val userPhoto: ImageView = findViewById(R.id.userPhoto)
        val projectDate: TextView = findViewById(R.id.projectDate)

// Show instructions to the user
        val instructions = "To view the results for a specific month:\n\n" +
                "1. Click on 'Current Month' to view the current month's results.\n" +
                "2. Click on 'Previous Month' to view the previous month's results.\n\n" +
                "Please note: To switch between months, you need to exit the project and come back."
        AlertDialog.Builder(this)
            .setTitle("Instructions")
            .setMessage(instructions)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()

        val project = intent.getSerializableExtra("project") as? Project

        if (project != null) {
            projectName.text = project.projectName
            projectCategory.text = projectCategory.text.toString() + project.category
            description.text = description.text.toString() + "\n" + project.workDescription
            hours.text =
                hours.text.toString() + "${project.minimumDailyHours} - ${project.maximumDailyHours}"
            projectDate.text = projectDate.text.toString() + project.date

            val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)

            // Check if the project has an imageUrl
            if (project.imageUrl.isNotEmpty()) {
                // Use Glide to load the image into the ImageView
                Glide.with(this)
                    .load(project.imageUrl)
                    .into(userPhoto)
            } else {
                // Set a default image or make the ImageView invisible
                userPhoto.setImageResource(R.drawable.person)
            }
            // Get references to the buttons
            val currentMonthButton: Button = findViewById(R.id.currentMonthButton)
            val previousMonthButton: Button = findViewById(R.id.previousMonthButton)


            currentMonthButton.setOnClickListener {
                currentMonthOffset = 0
                Log.d(
                    "Button Click",
                    "Current Month Button clicked. currentMonthOffset set to $currentMonthOffset"
                )
                setupChartForCurrentOffset()
            }

            previousMonthButton.setOnClickListener {
                currentMonthOffset -= 1
                Log.d(
                    "Button Click",
                    "Previous Month Button clicked. currentMonthOffset set to $currentMonthOffset"
                )
                setupChartForCurrentOffset()
            }
            // Get the current user ID
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val database = FirebaseDatabase.getInstance().getReference("TimeSheetEntries")

            database.child(currentUserId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if hoursMap is empty before generating random hours
                    if (hoursMap.isEmpty()) {
                        for (projectSnapshot in dataSnapshot.children) {
                            val project = projectSnapshot.getValue(Project::class.java)
                            if (project != null) {
                                val minHours = project.minimumDailyHours.toInt()
                                val maxHours = project.maximumDailyHours.toInt()

                                // Get or create the list associated with the current project's ID
                                val hoursList =
                                    hoursMap.getOrPut(projectSnapshot.key!!) { MutableList(7) { 0 } }

                                // Generate a random number of hours within the range of minHours and maxHours for each day of the week
                                for (i in 0 until 7) {
                                    val randomHours = (minHours..maxHours).random()
                                    hoursList[i] = randomHours
                                }
                            }
                        }
                    }

                    setupChartForCurrentOffset()

                    // Setup the initial chart with the current project date
                    project?.date?.let { setupChartForCurrentOffset() }


                }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }

    private fun setupChartForCurrentOffset() {
        Log.d("Chart Setup", "Setting up chart for currentMonthOffset $currentMonthOffset")
        for ((projectId, originalHoursList) in hoursMap) {
            val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)
            anyChartView.clear() // Clear the chart
            val project = intent.getSerializableExtra("project") as? Project
            if (project != null) {
                val minHours = project.minimumDailyHours.toInt()
                val maxHours = project.maximumDailyHours.toInt()

                // Generate random hours for each week when currentMonthOffset is less than 0
                val hours =
                    if (currentMonthOffset < 0) MutableList(4) { (minHours..maxHours).random() } else originalHoursList

                // Log the values of currentMonthOffset, hours, and originalHoursList
                Log.d("Chart Setup", "currentMonthOffset: $currentMonthOffset")
                Log.d("Chart Setup", "hours: $hours")
                Log.d("Chart Setup", "originalHoursList: $originalHoursList")

                ChartsActivity.ChartUtils.setupLineChart(
                    anyChartView,
                    hours,
                    minHours,
                    maxHours,
                    project.date,
                    currentMonthOffset
                )
            }
        }
    }
}