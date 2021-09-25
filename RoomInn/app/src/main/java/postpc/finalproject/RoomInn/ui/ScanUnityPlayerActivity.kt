package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity


class ScanUnityPlayerActivity : UnityPlayerActivity() {

    companion object{
        val sceneIndex = "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnityPlayer.UnitySendMessage("GameObject", "loadScene", sceneIndex)

    }

}