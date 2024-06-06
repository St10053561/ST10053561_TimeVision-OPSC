package com.example.timevision_application

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.timevision_application.R
import com.example.timevision_application.TimesheetEntry

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val timeSheetEntryButton = findViewById<ImageButton>(R.id.timeSheetEntryButton)
        timeSheetEntryButton.setOnClickListener {
            // Start TimesheetEntry activity when timeSheetEntryButton is clicked
            startActivity(Intent(this@HomeScreen, TimesheetEntry::class.java))
        }

        // Find the report button by its ID
        val reportButton = findViewById<ImageButton>(R.id.reportButton)
        reportButton.setOnClickListener {
            // Start TimeTrackerReport activity when reportButton is clicked
            startActivity(Intent(this@HomeScreen, TimeTrackerReport::class.java))
        }

        val absenceButton = findViewById<ImageButton>(R.id.absenceRequestButton)
        absenceButton.setOnClickListener {
            // Start AbsenceActivity when absenceButton is clicked
            startActivity(Intent(this@HomeScreen, AbsenceActivity::class.java))
        }

        val categoryButton = findViewById<ImageButton>(R.id.categoryButton)
        categoryButton.setOnClickListener {
            // Start AddCategoryActivity when categoryButton is clicked
            startActivity(Intent(this@HomeScreen, AddCategoryActivity::class.java))
        }
    }
}
