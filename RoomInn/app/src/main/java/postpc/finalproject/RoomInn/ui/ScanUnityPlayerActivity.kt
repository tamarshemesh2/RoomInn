package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.models.RoomInnApplication
import java.nio.file.Path
import kotlin.properties.Delegates


class ScanUnityPlayerActivity : UnityPlayerActivity() {
    private lateinit var userId: String
    private var returnTo by Delegates.notNull<Int>()
    private lateinit var unityPath: String

    companion object{
        val sceneIndex = "2"
        lateinit var ctx: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //sets the activity to free orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("User ID")!!
        returnTo = intent.getIntExtra("Return To",0)
        unityPath = intent.getStringExtra("Path To Unity")?:""
        unityPath =
            UnityPlayer.currentActivity.getExternalFilesDir("")!!.absolutePath
        Log.e("unityPathIs", unityPath)
        ctx = this


        UnityPlayer.UnitySendMessage("SceneLoader", "loadScene", sceneIndex)
    }

    fun toCallFromUnity() {
        // navigate to windows/ doors fragment
        runOnUiThread {
            val intent = Intent(this@ScanUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Room Name", "")
            intent.putExtra("Return To", returnTo)
            intent.putExtra("Path To Unity",unityPath)
            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

    fun onBackPressedUnity() {
        // navigate to windows/ doors fragment
        runOnUiThread {
            val intent = Intent(this@ScanUnityPlayerActivity, MainActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Room Name", "")
            intent.putExtra("Return To", 0)
            intent.putExtra("Path To Unity",unityPath)
            startActivity(intent)
            mUnityPlayer.quit()
        }
    }

}