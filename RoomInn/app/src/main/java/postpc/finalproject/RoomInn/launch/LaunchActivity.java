package postpc.finalproject.RoomInn.launch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel;
import postpc.finalproject.RoomInn.models.RoomInnApplication;

public class LaunchActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;

    private LoginViewModel viewModel;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_launch);
        FragmentContainerView fragment = findViewById(R.id.fragment_frame);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        if (viewModel.isRegistering()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragment.getId(), viewModel.getRegisterFragment())
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragment.getId(), viewModel.getLoginFragment())
                    .commit();
        }
    }

    // TODO: uncomment this! (do not delete this piece of code!)
    @Override
    protected void onStart() {
        super.onStart();
        String userId = getUserId();
        if (userId != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                    .addOnSuccessListener( v -> {
                        if (v.exists()) {
                            getToMainActivity(userId);
                            progressDialog.dismiss();
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            progressDialog.dismiss();
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnFailureListener(v -> {
                        FirebaseAuth.getInstance().signOut();
                        progressDialog.dismiss();
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT)
                                .show();
                    });
        } else {
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void getToMainActivity(String userId) {
        RoomInnApplication.getInstance().getRoomsDB().initialize(userId);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        // Result returned from launching the Intent from FacebookActivity;
        else {
            viewModel.getCallbackManager().onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            getToMainActivity(account.getId());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private String getUserId() {
        String userId = null;
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

        return userId;
    }

    @Override
    public void onBackPressed() {
        if (viewModel.isRegistering()) {
            viewModel.setRegistering(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(findViewById(R.id.fragment_frame).getId(), viewModel.getLoginFragment());
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }
}
