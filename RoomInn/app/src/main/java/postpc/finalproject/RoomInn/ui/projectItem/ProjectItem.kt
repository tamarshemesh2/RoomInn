package postpc.finalproject.RoomInn.ui.projectItem

import com.google.firebase.Timestamp


data class ProjectItem(
    val roomName: String,
) : Comparable<ProjectItem> {
    override fun compareTo(other: ProjectItem): Int {
        return roomName.compareTo(other.roomName)
    }
}