package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import postpc.finalproject.RoomInn.R

class ScanUnityHandler : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_handler)
        val unityButton = findViewById<Button>(R.id.startUnityButton)
        unityButton.setOnClickListener {
            var intent = Intent(this@ScanUnityHandler, MainUnityPlayerActivity::class.java)
            intent.putExtra("Scene Index", this.intent.getStringExtra("Scene Index"))
            startActivity(intent)
        }
    }
}