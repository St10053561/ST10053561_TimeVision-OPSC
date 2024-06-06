package com.example.timevision_application

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChartView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase

class ProjectDetailActivity : AppCompatActivity() {

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
        val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)

        val project = intent.getSerializableExtra("project") as? Project

        if (project != null) {
            projectName.text = project.projectName
            projectCategory.text = projectCategory.text.toString() + project.category
            description.text = description.text.toString() + "\n" + project.workDescription
            hours.text = hours.text.toString() + "${project.minimumDailyHours} - ${project.maximumDailyHours}"
            projectDate.text = projectDate.text.toString() + project.date

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

            val database = FirebaseDatabase.getInstance().getReference("TimeSheetEntries")
            database.child(currentUserId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (hoursMap.isEmpty()) {
                        for (projectSnapshot in dataSnapshot.children) {
                            val project = projectSnapshot.getValue(Project::class.java)
                            if (project != null) {
                                val minHours = project.minimumDailyHours.toInt()
                                val maxHours = project.maximumDailyHours.toInt()
                                val hoursList = hoursMap.getOrPut(projectSnapshot.key!!) { MutableList(7) { 0 } }
                                for (i in 0 until 7) {
                                    val randomHours = (minHours..maxHours).random()
                                    hoursList[i] = randomHours
                                }
                            }
                        }
                    }

                    for ((projectId, hoursList) in hoursMap) {

                        ChartsActivity.ChartUtils.setupLineChart(anyChartView, hoursList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("ProjectDetailActivity", "Error loading data: ${databaseError.message}")
                }
            })

            // Check if project contains image URL
            if (!project.imageUrl.isNullOrEmpty()) {
                val imageUrl = project.imageUrl
                Glide.with(this)
                    .load(imageUrl)
                    .into(userPhoto)
            } else {
                userPhoto.setImageResource(R.drawable.ic_add_image)
            }
        }
    }
}



