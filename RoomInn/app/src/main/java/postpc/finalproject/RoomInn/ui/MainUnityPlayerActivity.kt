package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import android.util.Log
import postpc.finalproject.RoomInn.furnitureData.Wall
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.models.RoomInnApplication


class MainUnityPlayerActivity : UnityPlayerActivity() {

    companion object {
        val sceneIndex = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sceneIndex = intent.getStringExtra("Scene Index")
        if (sceneIndex == ScanUnityPlayerActivity.sceneIndex) {
            UnityPlayer.UnitySendMessage(
                "SceneLoader",
                "loadScene",
                ScanUnityPlayerActivity.sceneIndex
            )
        } else {
            val roomName = intent.getStringExtra("Room Name")
            val roomDB = RoomInnApplication.getInstance().getRoomsDB()

        // render the walls.
        renderWalls(roomDB.wallsByRoomName(roomName!!))

        // render the furniture.
        renderFurniture(roomDB.roomFurniture(roomName))

        // render the doors.
        renderDoors(roomDB.doorsByRoomName(roomName))

        // render the windows.
        renderWindows(roomDB.windowsByRoomName(roomName))
        }


//        val wallList: MutableList<Wall> = mutableListOf()
//        wallList.add(Wall(Point3D(2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(0f, 0f, 3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(-2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
//        wallList.add(Wall(Point3D(0f, 0f, -3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))

//        val room = Room(mutableListOf<Point3D>(), wallList)
//        renderRoom(room)

    }

    private fun renderRoom(room: Room) {
        renderWalls(room.Walls)
//        renderFurniture(room.furniture)
        updateCeiling(room)
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
            UnityPlayer.UnitySendMessage("RigidBodyFPSController", furniture.unityFuncName,
                furniture.toString()
            )
        }
    }


//        UnityPlayer.UnitySendMessage(
//            "RigidBodyFPSController",
//            "addNewBed",
//            "(0,0,0)" + "\n" +
//                    "(0,0,0)" + "\n" +
//                    "(1.0,0.5,1.0)" + "\n" +
//                    "368")


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