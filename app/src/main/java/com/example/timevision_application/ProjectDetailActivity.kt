package com.example.timevision_application

import Project
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

class ProjectDetailActivity :
    AppCompatActivity() { // Define a new activity called ProjectDetailActivity
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call the superclass onCreate() method
        setContentView(R.layout.activity_project_detail) // Set the layout for this activity

        //Find the views by their IDs
        val projectName: TextView = findViewById(R.id.projectName)
        val projectCategory: TextView = findViewById(R.id.projectCategory)
        val description: TextView = findViewById(R.id.description)
        val hours: TextView = findViewById(R.id.hours)
        val userPhoto: ImageView = findViewById(R.id.userPhoto)
        val projectDate: TextView = findViewById(R.id.projectDate)

        // Retrieve the project object from the previous activity
        val project = intent.getSerializableExtra("project") as? Project

        if (project != null) {
            projectName.text = project.projectname
            projectCategory.text = projectCategory.text.toString() + project.category
            description.text = description.text.toString() + "\n" + project.description
            hours.text = hours.text.toString() + "${project.minHours} - ${project.maxHours}"
            userPhoto.setImageResource(project.userPhoto)
            projectDate.text = projectDate.text.toString() + project.projectDate

            //here I am linking the anyChartView to the layout to show the total hours of the project
            val anyChartView: AnyChartView = findViewById(R.id.any_chart_view)
            ChartsActivity.ChartUtils.setupLineChart(anyChartView, project.totalHours)
        }




    }

}