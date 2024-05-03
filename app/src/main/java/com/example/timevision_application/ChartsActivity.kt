package com.example.timevision_application


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import kotlin.random.Random

class ChartsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)


    }

    object ChartUtils {
        // Function to set up the line chart
        fun setupLineChart(anyChartView: AnyChartView, hoursList: List<Int>) {
            // Create a line chart
            val cartesian: Cartesian = AnyChart.line()

            // Initialize a list to store the data entries for the chart
            val data: MutableList<DataEntry> = ArrayList()
            // List of days of the week
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            // Loop through each day of the week
            for ((index, day) in daysOfWeek.withIndex()) {
                // Add a new data entry for each day with the hours from the hoursList
                data.add(ValueDataEntry(day, hoursList[index]))
            }

            // Create a set of data and add the data entries to it
            val set: com.anychart.data.Set = com.anychart.data.Set.instantiate()
            set.data(data)

            // Map the data for the line chart
            val lineData: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
            // Create a line series with the mapped data
            val series: Line = cartesian.line(lineData)

            // Set the stroke settings for the line
            series.stroke("blue", 2f, "10 5", "round", "round")

            // Set the title of the chart
            cartesian.title("Weekly Category Data")

            // Set the title of the x-axis and y-axis
            cartesian.xAxis(0).title("Days")
            cartesian.yAxis(0).title("Hours")

            // Enable tooltips for the chart
            cartesian.tooltip().enabled(true)

            // Set the chart in the AnyChartView
            anyChartView.setChart(cartesian)
        }
    }

}