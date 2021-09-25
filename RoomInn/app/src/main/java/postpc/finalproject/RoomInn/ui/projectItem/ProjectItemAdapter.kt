package postpc.finalproject.RoomInn.ui.projectItem

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.ui.MainUnityPlayerActivity
import postpc.finalproject.RoomInn.ui.UnityHandler
import postpc.finalproject.RoomInn.ui.gui_gestures.ProjectAdapterGesturesListener


class ProjectItemAdapter : RecyclerView.Adapter<ProjectItemHolder>() {

    private var _projects: MutableList<ProjectItem> = ArrayList()
    private var viewModel: ProjectViewModel? = null
    private val roomsDB = RoomInnApplication.getInstance().getRoomsDB()
    private lateinit var context: android.content.Context

    init {
        setItems()
    }

    fun setContext(context: android.content.Context) {
        this.context = context
    }

    fun setItems() {
        if (RoomInnApplication.getInstance().getRoomsDB().isInitialized()) {
            _projects.clear()
            val roomsNamesSet = RoomInnApplication.getInstance().getRoomsDB().rooms.value!!
            roomsNamesSet.forEach {
                _projects.add(ProjectItem(it))
            }
            notifyDataSetChanged()
        }
    }

    fun deleteProject(position: Int) {
        roomsDB.deleteRoom(_projects[position].roomName)
    }


    fun getItems(): List<String> {
        val mutableList = mutableListOf<String>()
        for (pro in _projects) {
            mutableList.add(pro.roomName)
        }
        return mutableList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItemHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_project, parent, false)
        return ProjectItemHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ProjectItemHolder, position: Int) {

        if (_projects.size == 0) {
            holder.playButton.visibility = View.GONE
            holder.editFabButton.visibility = View.GONE
            holder.projectName.gravity = Gravity.CENTER
            holder.projectName.text =
                "\n\nYou don't have any projects yet \n\n \uD83D\uDE31 \n\nPress on the add button below to get started!"
            holder.border.visibility = View.GONE

        } else {
            val projectItem = _projects[position]
            holder.playButton.visibility = View.VISIBLE
            holder.editFabButton.visibility = View.VISIBLE
            holder.projectName.gravity = Gravity.START
            holder.border.visibility = View.VISIBLE
            holder.projectName.text = projectItem.roomName
            holder.bg.setOnTouchListener(
                ProjectAdapterGesturesListener(
                    context,
                    holder.delButton,
                    viewModel!!.adapter,
                    holder,
                    projectItem
                )
            )


            holder.editFabButton.setOnClickListener {
                RoomInnApplication.getInstance().getRoomsDB()
                    .loadRoomByName(projectItem.roomName, viewModel)
                viewModel!!.projectName = projectItem.roomName
            }

            holder.playButton.setOnClickListener {
                val intent = Intent(context, UnityHandler::class.java)
                intent.putExtra("Scene Index", MainUnityPlayerActivity.sceneIndex)
                intent.putExtra("Room ID", viewModel!!.room.id)
                context.startActivity(intent)

            }
        }

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