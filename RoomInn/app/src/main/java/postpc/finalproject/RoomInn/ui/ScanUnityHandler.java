package postpc.finalproject.RoomInn.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.unity3d.player.UnityPlayer;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;
import postpc.finalproject.RoomInn.models.RoomInnApplication;

public class ScanUnityHandler extends AppCompatActivity {
    public static Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_unity_handler);
        Button unityButton = findViewById(R.id.startUnityButton);
        unityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent thisIntent = getIntent();
                Intent intent = new Intent(ScanUnityHandler.this, ScanUnityPlayerActivity.class);
                intent.putExtra("Scene Index", thisIntent.getStringExtra("Scene Index"));
                startActivity(intent);
            }
        });
    }


    public void backPressedFromRoomScan(){
        // navigate to project page
        super.onBackPressed();
    }

    public void toCallFromUnity(){
        // navigate to windows/ doors fragment

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(ScanUnityHandler.this, MainActivity.class);
                String userId = getIntent().getStringExtra("User ID");
                String roomName =  getIntent().getStringExtra("Room Name");
                intent.putExtra("User ID", userId);
                intent.putExtra("Room Name",roomName);
                startActivity(intent);
//                this.mUnityPlayer.quit();

            }
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null){
            NavController navController = navHostFragment.getNavController();
            navController.navigate(R.id.action_profileFragment2_to_floorPlanPlacingFragment);
        }
    }
}