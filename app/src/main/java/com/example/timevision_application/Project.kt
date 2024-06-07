package com.example.timevision_application

import java.io.Serializable

data class Project(
    var projectName: String = "",
    var category: String = "",
    var date: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var minimumDailyHours: String = "",
    var maximumDailyHours: String = "",
    var totalDuration: String = "",
    var workDescription: String = "",
    var imageUrl: String = ""
) : Serializable
