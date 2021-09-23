package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance

class UnityHandler : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_handler)
        val unityButton = findViewById<Button>(R.id.startUnityButton)
        unityButton.setOnClickListener {
            val sceneIndex = intent.getStringExtra("Scene Index")
            var intent: Intent
            if (sceneIndex == "1") {
                intent = Intent(this@UnityHandler, RoomUnityPlayerActivity::class.java)
            }
            else {
                intent = Intent(this@UnityHandler, ScanUnityPlayerActivity::class.java)
            }
            startActivity(intent)
            getInstance().pathToUnity = UnityPlayer.currentActivity.getExternalFilesDir("")!!
                .absolutePath
        }
    }
}