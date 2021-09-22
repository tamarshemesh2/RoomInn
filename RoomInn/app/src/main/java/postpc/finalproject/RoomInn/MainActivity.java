package postpc.finalproject.RoomInn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import postpc.finalproject.RoomInn.ViewModle.LoginViewModel;
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel;
import postpc.finalproject.RoomInn.models.LoadingStage;
import postpc.finalproject.RoomInn.models.RoomInnApplication;
import postpc.finalproject.RoomInn.models.RoomsDB;

public class MainActivity extends AppCompatActivity {
    private String userId = null;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog =  new ProgressDialog(this);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            userId = acct.getId();
            Log.d("login", "login with google id: " + userId);
        }

        // user  is already logged in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            userId = accessToken.getUserId();
            Log.d("login", "login with facebook id: " + userId);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            Log.d("login", "login with firebase id: " + userId);

        }

        ProjectViewModel viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        listenToLoadingStage();
        initializeDB();
//        RoomInnApplication.getInstance().getRoomsDB().setRoomsListListener(this);

    }

    private void initializeDB() {
        if (userId != null) {
            RoomInnApplication.getInstance().getRoomsDB().initialize(userId);
            Toast.makeText(this, "Loaded user" , Toast.LENGTH_LONG).show();
        } else {
            Log.d("Firebase", "user in null");
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
    protected void onDestroy() {
        RoomsDB DB = RoomInnApplication.getInstance().getRoomsDB();
        DB.getRooms().removeObservers(this);
        DB.getUserLoadingStage().removeObservers(this);
        DB.saveOnExit();
        super.onDestroy();
    }


}