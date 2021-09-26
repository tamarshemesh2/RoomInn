package postpc.finalproject.RoomInn.ui.projectItem

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import postpc.finalproject.RoomInn.R

class ProjectItemHolder(view: View) : RecyclerView.ViewHolder(view){
    val projectName: TextView = view.findViewById(R.id.project_name)
    val projectNameEditText: TextView = view.findViewById(R.id.edit_project_name)
    val editFabButton: FloatingActionButton = view.findViewById(R.id.edit_fab)
    val playButton: ImageButton = view.findViewById(R.id.play_button)
    val delButton: ImageButton = view.findViewById(R.id.delete_project_button)
    val border: TextView = view.findViewById(R.id.border_line)
    val bg:ConstraintLayout = view.findViewById(R.id.project_layout)}
