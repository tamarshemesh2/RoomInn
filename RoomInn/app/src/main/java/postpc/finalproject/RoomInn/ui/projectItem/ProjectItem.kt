package postpc.finalproject.RoomInn.ui.projectItem

data class ProjectItem(
    val roomName: String,
) : Comparable<ProjectItem> {
    override fun compareTo(other: ProjectItem): Int {
        return roomName.compareTo(other.roomName)
    }
}