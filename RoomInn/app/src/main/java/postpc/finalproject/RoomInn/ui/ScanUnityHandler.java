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

import postpc.finalproject.RoomInn.R;

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
                Intent intent = new Intent(ScanUnityHandler.this, MainUnityPlayerActivity.class);
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
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null){
            NavController navController = navHostFragment.getNavController();
            navController.navigate(R.id.action_profileFragment2_to_floorPlanPlacingFragment);
        }
    }
}