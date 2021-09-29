package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.os.Bundle
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.models.RoomInnApplication


class ScanUnityPlayerActivity : UnityPlayerActivity() {
    private lateinit var userId: String

    companion object{
        val sceneIndex = "2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("userId")!!

        RoomInnApplication.getInstance().pathToUnity.value =
            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath + "/"

        UnityPlayer.UnitySendMessage("GameObject", "loadScene", sceneIndex)
    }

    fun toCallFromUnity() {
        // navigate to windows/ doors fragment
        runOnUiThread {
            val intent = Intent(this@ScanUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("roomName", "")
            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

}