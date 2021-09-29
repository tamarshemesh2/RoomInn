package postpc.finalproject.RoomInn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel;
import postpc.finalproject.RoomInn.models.LoadingStage;
import postpc.finalproject.RoomInn.models.RoomInnApplication;
import postpc.finalproject.RoomInn.models.RoomsDB;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog =  new ProgressDialog(this);
        ProjectViewModel viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        viewModel.setActivityContext(this);
        listenToLoadingStage();

        // todo - add the function of tamar, initialize by user id and room name
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String userId = intent.getStringExtra("User ID");
            String roomName = intent.getStringExtra("Room Name");
            RoomInnApplication.getInstance().getRoomsDB().initializeAfterUnity(userId,roomName, viewModel);
        }

    }

    private void listenToLoadingStage() {
        RoomInnApplication.getInstance().getRoomsDB().getUserLoadingStage().observe(
                this,
                new Observer<LoadingStage>() {
                    @Override
                    public void onChanged(LoadingStage loadingStage) {
                        if (loadingStage == LoadingStage.LOADING) {
                            progressDialog.setTitle("Fetching Project");
                            progressDialog.setMessage("Please wait...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                }
        );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("main activity", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        RoomsDB DB = RoomInnApplication.getInstance().getRoomsDB();
        DB.saveOnExit();
    }

    @Override
    protected void onDestroy() {
        Log.d("main activity", "onDestroy");
        RoomsDB DB = RoomInnApplication.getInstance().getRoomsDB();
        DB.getRooms().removeObservers(this);
        DB.getUserLoadingStage().removeObservers(this);
        DB.saveOnExit();
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        Log.d("main activity", "onPause");
        RoomsDB DB = RoomInnApplication.getInstance().getRoomsDB();
        DB.saveOnExit();
        super.onPause();
    }

}