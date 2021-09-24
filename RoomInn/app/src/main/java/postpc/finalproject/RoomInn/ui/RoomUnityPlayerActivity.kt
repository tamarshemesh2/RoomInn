package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import android.util.Log
import com.postpc.myapplication.furnitureData.Wall
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.models.RoomInnApplication


class RoomUnityPlayerActivity : UnityPlayerActivity() {

    companion object{
        val sceneIndex = "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val roomName = intent.getStringExtra("Room Name")
        val roomDB = RoomInnApplication.getInstance().getRoomsDB()
        UnityPlayer.UnitySendMessage("GameObject", "loadScene", sceneIndex)

        Log.e("roomid", roomDB.toString())

        // render the walls.
        renderWalls(roomDB.wallsByRoomName(roomName!!))

        // render the furniture.
        renderFurniture(roomDB.roomFurniture(roomName))

        // render the doors.
        renderDoors(roomDB.doorsByRoomName(roomName))

        // render the windows.
        renderWindows(roomDB.windowsByRoomName(roomName))





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
            UnityPlayer.UnitySendMessage("FPSController", "addNewWall", wall.toString())
        }
    }

    private fun updateCeiling(room: Room) {
        UnityPlayer.UnitySendMessage("FPSController", "updateCeillingHight", "0.6")
    }

    private fun renderFurniture(furnitureList: MutableList<Furniture>) {
        for (furniture in furnitureList) {
            Log.e("funiture",furniture.toString() )
            UnityPlayer.UnitySendMessage(
                "FPSController",
                furniture.unityFuncName,
                furniture.toString()
            )
        }
    }

    private fun renderDoors(doorList: MutableList<Door>) {
        for (door in doorList) {
            UnityPlayer.UnitySendMessage("FPSController", "addNewDoor", door.toString())
        }
    }

    private fun renderWindows(windowList: MutableList<Window>) {
        for (window in windowList) {
            UnityPlayer.UnitySendMessage("FPSController", "addNewWindow", window.toString())
        }
    }
}