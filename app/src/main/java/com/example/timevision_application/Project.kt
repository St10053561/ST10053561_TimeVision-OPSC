import java.io.Serializable

data class Project(
    val projectname: String,
    val category: String,
    val description : String,
    val minHours: Int,
    val maxHours: Int,
    val userPhoto: String,
    val projectDate: String,
    val totalHours: List<Int> // Adding a property for each day hours of a week.
) : Serializable