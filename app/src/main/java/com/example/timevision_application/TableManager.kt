package com.example.timevision_application

import Project
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat

class TableManager(private val activity: MainActivity, private val tableLayout : TableLayout) {

    fun populateTable(projects: List<Project>){

        while(tableLayout.childCount > 1){
            tableLayout.removeViewAt(tableLayout.childCount - 1)
        }

        for(project in projects){
            val tableRow = TableRow(activity).apply{

                background = ContextCompat.getDrawable(activity, R.drawable.table_row_background)
                addView(TextView(activity).apply {
                    text = project.projectName
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
                })
                addView(TextView(activity).apply {
                    text = project.category
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
                })
                addView(TextView(activity).apply {
                    text = project.date
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
                })
                addView(TextView(activity).apply {
                    text = project.status
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
                })
                addView(TextView(activity).apply {
                    text = project.startEnd
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                })
                addView(TextView(activity).apply {
                    text = project.totalHours.toString()
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                })
                addView(TextView(activity).apply {
                    text = project.minDailyGoal.toString()
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                })
                addView(TextView(activity).apply {
                    text = project.maxDailyGoal.toString()
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                })
                addView(TextView(activity).apply {
                    text = project.photo
                    gravity = Gravity.CENTER
                    typeface = Typeface.MONOSPACE
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
                })
            }
            tableLayout.addView(tableRow)
        }
    }


}




