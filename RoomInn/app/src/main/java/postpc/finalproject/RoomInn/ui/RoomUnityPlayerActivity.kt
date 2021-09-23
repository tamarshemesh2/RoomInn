package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import com.postpc.myapplication.furnitureData.Wall
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.*


class RoomUnityPlayerActivity : UnityPlayerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UnityPlayer.UnitySendMessage("GameObject", "loadScene", "1")

        val wallList: MutableList<Wall> = mutableListOf()
        wallList.add(Wall(Point3D(2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
        wallList.add(Wall(Point3D(0f, 0f, 3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))
        wallList.add(Wall(Point3D(-2.5f, 0f, 0f), Point3D(0f, 90f, 0f), Point3D(6f, 10f, 0.0001f)))
        wallList.add(Wall(Point3D(0f, 0f, -3f), Point3D(0f, 0f, 0f), Point3D(5f, 10f, 0.0001f)))


//        val furnitureList :MutableList<Furniture> = mutableListOf()
//        furnitureList.add(Bed(Point3D(-0.75f,0f,1f),Point3D(0f,180f,0f),Point3D(1f,1f,1f)))
//        furnitureList.add(Closet(Point3D(0.75f,0f,1.75f),Point3D(0f,180f,0f),Point3D(1f,1f,1f)))
//        furnitureList.add(Desk(Point3D(-1.2f,0f,-1.25f),Point3D(0f,90f,0f),Point3D(1f,1f,1f)))
//        furnitureList.add(Chair(Point3D(-1f,0f,-1.25f),Point3D(0f,0f,0f),Point3D(1f,1f,1f)))
        val room = Room(mutableListOf<Point3D>(), wallList)
        renderRoom(room)

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
            UnityPlayer.UnitySendMessage(
                "FPSController",
                furniture.unityFuncName,
                furniture.toString()
            )
        }
    }

    private fun renderDoors(doorList: MutableList<Furniture>) {
        for (door in doorList) {
            UnityPlayer.UnitySendMessage("FPSController", "addNewDoor", door.toString())
        }
    }

    private fun renderWindows(windowList: MutableList<Furniture>) {
        for (window in windowList) {
            UnityPlayer.UnitySendMessage("FPSController", "addNewWindow", window.toString())
        }
    }
}