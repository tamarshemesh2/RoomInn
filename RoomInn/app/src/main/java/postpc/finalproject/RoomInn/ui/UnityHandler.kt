package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import postpc.finalproject.RoomInn.R

class UnityHandler : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_handler)
        val unityButton = findViewById<Button>(R.id.startUnityButton)
        unityButton.setOnClickListener {
            var intent = Intent(this@UnityHandler, MainUnityPlayerActivity::class.java)
            intent.putExtra("Scene Index", this.intent.getStringExtra("Scene Index"))
            intent.putExtra("Room Name", this.intent.getStringExtra("Room Name"))
            startActivity(intent)
//            if (sceneIndex == RoomUnityPlayerActivity.sceneIndex) {
//                UnityPlayer.UnitySendMessage("ScenceLoader", "loadScene",
//                    RoomUnityPlayerActivity.sceneIndex
//                )
//                intent = Intent(this@UnityHandler, RoomUnityPlayerActivity::class.java)
//                intent.putExtra("Room Name", this.intent.getStringExtra("Room Name"))
//            }
//            else {
//                intent = Intent(this@UnityHandler, ScanUnityPlayerActivity::class.java)
//            }
//            startActivity(intent)
//            getInstance().pathToUnity = UnityPlayer.currentActivity.getExternalFilesDir("")!!
//                .absolutePath
        }
    }
}