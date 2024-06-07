package com.example.timevision_application

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import java.util.*

class ChartsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
    }

    object ChartUtils {
        fun setupLineChart(
            anyChartView: AnyChartView,
            hoursList: List<Int>,
            minHours: Int,
            maxHours: Int,
            projectDate: String,
            monthOffset: Int
        ) {
            Log.d(
                "Chart Setup",
                "Setting up chart in ChartsActivity. Received monthOffset: $monthOffset"
            )
            anyChartView.clear()
            val cartesian: Cartesian = AnyChart.line()
            val data: MutableList<DataEntry> = ArrayList()
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            val weeksOfMonth = listOf("Week 1", "Week 2", "Week 3", "Week 4")

            val labels = if (monthOffset < 0) weeksOfMonth else daysOfWeek
            for ((index, label) in labels.withIndex()) {
                val hours =
                    if (index < hoursList.size) hoursList[index] else 0 // Default value if hoursList is not long enough
                data.add(ValueDataEntry(label, hours))
            }


            val set: com.anychart.data.Set = com.anychart.data.Set.instantiate()
            set.data(data)
            val lineData: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
            val series: Line = cartesian.line(lineData)
            series.stroke("blue", 2f, "10 5", "round", "round")

            // Create data for min and max lines
            val minLineData = labels.map { ValueDataEntry(it, minHours) }
            val maxLineData = labels.map { ValueDataEntry(it, maxHours) }
            cartesian.line(minLineData).stroke("2 red")
            cartesian.line(maxLineData).stroke("2 green")

            // Update the chart title
            updateChartTitle(cartesian, projectDate, monthOffset)

            cartesian.xAxis(0).title(if (monthOffset < 0) "Weeks" else "Days")
            cartesian.yAxis(0).title("Hours")
            cartesian.yScale().ticks().interval(1)
            cartesian.tooltip().enabled(true)
            anyChartView.setChart(cartesian)
        }

        private fun updateChartTitle(cartesian: Cartesian, projectDate: String, monthOffset: Int) {
            // Parse the project date to get the month name
            val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
            val date = inputFormat.parse(projectDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MONTH, monthOffset)
            val monthName = outputFormat.format(calendar.time)

            cartesian.title(if (monthOffset < 0) "$monthName Weeks Data" else "$monthName Days Data")
        }
    }
}


