data class Project(
    // Iam creating a variables for each field in the database
    val projectName: String,
    val category: String,
    val date: String,
    val status: String,
    val startEnd: String,
    val totalHours: Float,
    val minDailyGoal: Int,
    val maxDailyGoal: Int,
    val photo: String
)