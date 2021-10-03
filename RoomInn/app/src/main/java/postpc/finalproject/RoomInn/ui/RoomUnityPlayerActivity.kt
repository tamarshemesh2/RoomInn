package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.Door
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Wall
import postpc.finalproject.RoomInn.furnitureData.Window
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.models.RoomsDB
import kotlin.properties.Delegates


class RoomUnityPlayerActivity : UnityPlayerActivity() {

    companion object {
        val sceneIndex = "1"
        lateinit var ctx: Context
    }

    lateinit var roomName: String
    lateinit var userId: String
    lateinit var roomDB: RoomsDB
    var returnTo by Delegates.notNull<Int>()
    private lateinit var unityPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        //sets the activity to sensor orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        super.onCreate(savedInstanceState)

        roomName = intent.getStringExtra("Room Name")!!
        roomDB = RoomInnApplication.getInstance().getRoomsDB()
        userId = intent.getStringExtra("User ID")!!
        returnTo = intent.getIntExtra("Return To", 0)

        ctx = this
        unityPath =
            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath
        Log.e("unityPathIs", unityPath)

        Log.d(
            "updateRoom",
            "in unity player activity, roomsMap is ${
                RoomInnApplication.getInstance().getRoomsDB().roomsMap
            }"
        )

        UnityPlayer.UnitySendMessage(
            "SceneLoader",
            "loadScene",
            RoomUnityPlayerActivity.sceneIndex
        )
    }

    fun existUnityActivity() {
        runOnUiThread {
            val intent = Intent(this@RoomUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Room Name", roomName)
            intent.putExtra("Return To", returnTo)
            intent.putExtra("Path To Unity", unityPath)

            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

    fun renderRoom() {

        Log.e("roomCenter", roomDB.roomByRoomName(roomName).roomCenterGetter().toString())

        //render the walls.
        renderWalls(roomDB.wallsByRoomName(roomName!!))

        // render the furniture.
        renderFurniture(roomDB.roomFurniture(roomName))

        // render the doors.
        renderDoors(roomDB.doorsByRoomName(roomName))

        // render the windows.
        renderWindows(roomDB.windowsByRoomName(roomName))
    }

    private fun renderWalls(wallList: MutableList<Wall>) {
        for (wall in wallList) {
            Log.e("Wall", wall.toString())
            UnityPlayer.UnitySendMessage(
                "RigidBodyFPSController",
                "addNewWall",
                wall.toString()
            )
        }
    }

    private fun updateCeiling(room: Room) {
        UnityPlayer.UnitySendMessage("RigidBodyFPSController", "updateCeillingHight", "0.6")
    }

    private fun renderFurniture(furnitureList: MutableList<Furniture>) {
        for (furniture in furnitureList) {
            Log.e("furniture", furniture.toString())
            UnityPlayer.UnitySendMessage(
                "RigidBodyFPSController", furniture.unityType.unityFuncName,
                furniture.toString()
            )
        }
    }

    private fun renderDoors(doorList: MutableList<Door>) {
        for (door in doorList) {
            Log.e("Door", door.toString())
            UnityPlayer.UnitySendMessage("RigidBodyFPSController", "addNewDoor", door.toString())
        }
    }

    private fun renderWindows(windowList: MutableList<Window>) {
        for (window in windowList) {
            Log.e("Window", window.toString())
            UnityPlayer.UnitySendMessage(
                "RigidBodyFPSController",
                "addNewWindow",
                window.toString()
            )
        }
    }
}