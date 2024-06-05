package com.example.timevision_application

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProjectDetailActivity : AppCompatActivity() {

    // Declare hoursMap as a member variable
    private val hoursMap = HashMap<String, MutableList<Int>>()

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

        val project = intent.getSerializableExtra("project") as? Project

        if (project != null) {
            projectName.text = project.projectName
            projectCategory.text = projectCategory.text.toString() + project.category
            description.text = description.text.toString() + "\n" + project.workDescription
            hours.text =
                hours.text.toString() + "${project.minimumDailyHours} - ${project.maximumDailyHours}"
            projectDate.text = projectDate.text.toString() + project.date

            val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)
            // Assuming totalDuration is a comma-separated string of integers
            val totalDurationList = if (project.totalDuration.isNotEmpty()) {
                project.totalDuration.split(",").map { it.trim().toInt() }
            } else {
                emptyList()
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

                    // Iterate over the hoursMap and setup the line chart for each project
                    for ((projectId, hoursList) in hoursMap) {
                        val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)
                        ChartsActivity.ChartUtils.setupLineChart(anyChartView, hoursList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })

            // Get a reference to the ImageView
            val userPhoto: ImageView = findViewById(R.id.userPhoto)

            // Get a reference to the Firebase Storage instance
            val storage = Firebase.storage


// Provide the path to the image within the bucket
            val imageRef =
                storage.reference.child("images/OoLYzP9VPTa97vuUzOW3wijgjFv1/83c34f93-db6b-4d5d-a4ae-6fb250893ac1")

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Use Glide to load the image into the ImageView
                Glide.with(this /* context */)
                    .load(uri)
                    .into(userPhoto)
            }.addOnFailureListener {
                // Load default image from drawable when image download fails
                userPhoto.setImageResource(R.drawable.person)
            }

        }
    }
}