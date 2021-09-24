package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import com.postpc.myapplication.furnitureData.Wall
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.furnitureData.*


class ScanUnityPlayerActivity : UnityPlayerActivity() {

    companion object{
        val sceneIndex = "2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnityPlayer.UnitySendMessage("GameObject", "loadScene", sceneIndex)

    }

}