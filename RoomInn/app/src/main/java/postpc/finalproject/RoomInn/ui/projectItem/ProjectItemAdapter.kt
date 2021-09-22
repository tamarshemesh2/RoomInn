package postpc.finalproject.RoomInn.ui.projectItem

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication


class ProjectItemAdapter : RecyclerView.Adapter<ProjectItemHolder>() {

    private var _projects: MutableList<ProjectItem> = ArrayList()
    private var viewModel: ProjectViewModel? =null

    init {
        setItems()
    }

    fun setItems() {
        if (RoomInnApplication.getInstance().getRoomsDB().isInitialized()) {
            _projects.clear()
            val roomsNamesSet = RoomInnApplication.getInstance().getRoomsDB().rooms.value!!
            roomsNamesSet.forEach {
                _projects.add(ProjectItem(it))
            }
            Log.d("Rooms List: ", "list form adapter is ${roomsNamesSet}.")
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItemHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_project, parent, false)
        return ProjectItemHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectItemHolder, position: Int) {
        if (_projects.size == 0) {
            holder.playButton.visibility = View.GONE
            holder.editFabButton.visibility = View.GONE
            holder.projectName.gravity = Gravity.CENTER
            holder.projectName.text = "\n\nYou don't have any projects yet \n\n \uD83D\uDE31 \n\nPress on the add button below to get started!"
            holder.border.visibility = View.GONE

        }
        else {
            holder.playButton.visibility = View.VISIBLE
            holder.editFabButton.visibility = View.VISIBLE
            holder.projectName.gravity = Gravity.START
            holder.border.visibility = View.VISIBLE
            val projectItem = _projects[position]
            holder.projectName.text = projectItem.roomName


            holder.editFabButton.setOnClickListener {
                RoomInnApplication.getInstance().getRoomsDB().loadRoomByName(projectItem.roomName, viewModel)
                viewModel!!.projectName = projectItem.roomName
            }
        }

    //TODO:
    // 1. add option to edit the name of the project.
    // 2. add the 'play' button on click (after we create the play VR & fragment).
    // 3. adding option to delete project using 'fling' (ask Yuval what the hell is fling)

    }

    override fun getItemCount(): Int {
        if (_projects.size == 0) {
            return 1
        }
        return _projects.size
    }

    fun setViewModel(viewModel: ProjectViewModel) {
        this.viewModel = viewModel
    }

}