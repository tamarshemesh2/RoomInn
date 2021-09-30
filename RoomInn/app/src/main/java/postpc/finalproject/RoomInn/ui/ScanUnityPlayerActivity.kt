package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.models.RoomInnApplication
import kotlin.properties.Delegates


class ScanUnityPlayerActivity : UnityPlayerActivity() {
    private lateinit var userId: String
    private var returnTo by Delegates.notNull<Int>()

    companion object{
        val sceneIndex = "2"
        lateinit var ctx: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("User ID")!!
        returnTo = intent.getIntExtra("Return To",0)

        ctx = this

        RoomInnApplication.getInstance().pathToUnity.value =
            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath + "/"

        UnityPlayer.UnitySendMessage("SceneLoader", "loadScene", sceneIndex)
    }

    fun toCallFromUnity() {
        // navigate to windows/ doors fragment
        runOnUiThread {
            val intent = Intent(this@ScanUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Room Name", "")
            intent.putExtra("Return To", returnTo)
            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

}