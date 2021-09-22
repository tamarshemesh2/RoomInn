package postpc.finalproject.RoomInn.Launch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel;

public class LaunchActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;

    private Fragment loginFragment;
    private Fragment registerFragment;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_launch);
        FragmentContainerView fragment = findViewById(R.id.fragment_frame);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(fragment.getId(), loginFragment)
                .commit();
    }

    // TODO: uncomment this! (do not delete this piece of code!)
    @Override
    protected void onStart() {
        // this function will activate if the user signed up with google before
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        getToMainActivity();
    }

    private void getToMainActivity() {
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
            getToMainActivity();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
