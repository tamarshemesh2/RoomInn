package postpc.finalproject.RoomInn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import postpc.finalproject.RoomInn.R;
import postpc.finalproject.RoomInn.models.RoomInnApplication;


public class UnityHandler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_handler);

        Button unityButton = findViewById(R.id.startUnityButton);
        unityButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(UnityHandler.this, UnityPlayerActivity.class);
                   startActivity(intent);
                   RoomInnApplication.getInstance().setPathToUnity(UnityPlayer.currentActivity.getExternalFilesDir("").getAbsolutePath());
               }
           }
        );
    }
}