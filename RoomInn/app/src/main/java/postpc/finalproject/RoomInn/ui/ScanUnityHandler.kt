package postpc.finalproject.RoomInn.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ui.ScanUnityHandler

class ScanUnityHandler : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var thisIntent: Intent

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_handler)
        val unityButton = findViewById<Button>(R.id.startUnityButton)
        unityButton.setOnClickListener {
            thisIntent = intent
            userId = thisIntent.getStringExtra("User ID")!!
            val intent = Intent(this@ScanUnityHandler, ScanUnityPlayerActivity::class.java)
            intent.putExtra("User ID", userId)
            startActivity(intent)
        }
    }
}