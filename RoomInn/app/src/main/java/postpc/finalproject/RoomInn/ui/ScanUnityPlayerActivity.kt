package postpc.finalproject.RoomInn.ui

import android.os.Bundle
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.models.RoomInnApplication


class ScanUnityPlayerActivity : UnityPlayerActivity() {

    companion object{
        val sceneIndex = "2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RoomInnApplication.getInstance().pathToUnity.value =
            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath + "/"

        UnityPlayer.UnitySendMessage("GameObject", "loadScene", sceneIndex)

    }

}