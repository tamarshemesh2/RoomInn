package postpc.finalproject.RoomInn.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import postpc.finalproject.RoomInn.R
import kotlin.properties.Delegates

class ScanUnityHandler : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var thisIntent: Intent
    private var returnTo by Delegates.notNull<Int>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        //sets the activity to free orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_handler)
        val unityButton = findViewById<Button>(R.id.startUnityButton)
        unityButton.setOnClickListener {
            thisIntent = intent
            userId = thisIntent.getStringExtra("User ID")!!
            returnTo = thisIntent.getIntExtra("Return To",0)
            val intent = Intent(this@ScanUnityHandler, ScanUnityPlayerActivity::class.java)
            intent.putExtra("User ID", userId)
            intent.putExtra("Return To", returnTo)
            startActivity(intent)
        }
    }
}