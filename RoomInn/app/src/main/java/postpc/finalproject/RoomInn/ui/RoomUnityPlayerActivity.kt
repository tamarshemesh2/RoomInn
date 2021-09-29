package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.content.Intent
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


class RoomUnityPlayerActivity : UnityPlayerActivity() {

    companion object {
        val sceneIndex = "1"
        lateinit var ctx: Context
    }

    lateinit var roomName : String
    lateinit var userId : String
    lateinit var roomDB : RoomsDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomName = intent.getStringExtra("Room Name")!!
        roomDB = RoomInnApplication.getInstance().getRoomsDB()
        userId =  intent.getStringExtra("User ID")!!

        ctx = this

        UnityPlayer.UnitySendMessage(
                "SceneLoader",
                "loadScene",
                RoomUnityPlayerActivity.sceneIndex)

//        getInstance().pathToUnity.value =
//            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath + "/"
//        val sceneIndex = intent.getStringExtra("Scene Index")
//        if (sceneIndex == ScanUnityPlayerActivity.sceneIndex) {
//            UnityPlayer.UnitySendMessage(
//                "SceneLoader",
//                "loadScene",
//                ScanUnityPlayerActivity.sceneIndex
//            )
//        } else {
//            val roomName = intent.getStringExtra("Room Name")
//            val roomDB = RoomInnApplication.getInstance().getRoomsDB()
//            UnityPlayer.UnitySendMessage(
//                "SceneLoader",
//                "loadScene",
//                MainUnityPlayerActivity.sceneIndex)
//
//        // render the walls.
//        renderWalls(roomDB.wallsByRoomName(roomName!!))
//
//        // render the furniture.
//        renderFurniture(roomDB.roomFurniture(roomName))
//
//        // render the doors.
//        renderDoors(roomDB.doorsByRoomName(roomName))
//
//        // render the windows.
//        renderWindows(roomDB.windowsByRoomName(roomName))
    //}


//        val wallList: MutableList<Wall> = mutableListOf()
//        wallList.add(Wall(Point3D(2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(0f, 0f, 3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(-2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(0f, 0f, -3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))

//        val room = Room(mutableListOf<Point3D>(), wallList)
//        renderRoom(room)

    }

    fun existUnityActivity() {
        runOnUiThread {
            val intent = Intent(this@RoomUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Room Name", roomName)
            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

    fun renderRoom() {
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
            UnityPlayer.UnitySendMessage("RigidBodyFPSController", "addNewWall", wall.toString())
        }
    }

    private fun updateCeiling(room: Room) {
        UnityPlayer.UnitySendMessage("RigidBodyFPSController", "updateCeillingHight", "0.6")
    }

    private fun renderFurniture(furnitureList: MutableList<Furniture>) {
        for (furniture in furnitureList) {
            Log.e("funiture", furniture.toString())
            UnityPlayer.UnitySendMessage("RigidBodyFPSController", furniture.unityType.unityFuncName,
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
            UnityPlayer.UnitySendMessage("RigidBodyFPSController", "addNewWindow", window.toString())
        }
    }
}