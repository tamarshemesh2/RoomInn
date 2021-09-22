// TODO: delete this file

package postpc.finalproject.RoomInn.Launch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;


public class LoginActivity extends AppCompatActivity{
    int RC_SIGN_IN = 0;
    String validateEmailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton facebookLogInButton;
    Button registerButton;
    EditText inputEmail;
    EditText inputPassword;
    Button logInButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        super.onCreate(savedInstanceState);

        // user  is already logged in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            getToMainActivity();
        }

        setContentView(R.layout.activity_login);


        // setup login with google:
        SignInButton googleLogInButton = findViewById(R.id.google_log_in_button);
        googleLogInButton.setEnabled(false);
        googleLogInButton.setVisibility(View.GONE);

        ImageView googleViewButton = findViewById(R.id.google_image);
        googleViewButton.setEnabled(true);
        googleViewButton.setVisibility(View.VISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleLogInButton.setOnClickListener(v -> {
            if (v.getId() == R.id.google_log_in_button) {
                googleSignIn();
            }
        });

        googleViewButton.setOnClickListener(v -> {
            googleLogInButton.callOnClick();
        });


        // setup login with facebook:
        callbackManager = CallbackManager.Factory.create();
        facebookLogInButton = findViewById(R.id.facebook_log_in_button);
        facebookLogInButton.setEnabled(false);
        facebookLogInButton.setVisibility(View.GONE);

        ImageView facebookViewButton = findViewById(R.id.facebook_image);
        facebookViewButton.setEnabled(true);
        facebookViewButton.setVisibility(View.VISIBLE);


//        facebookLogInButton.setPermissions(Arrays.asList(...));
        facebookLogInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook login", "Success");
                getToMainActivity();
            }

            @Override
            public void onCancel() {
                Log.d("facebook login", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook login", error.toString());
            }
        });

        facebookViewButton.setOnClickListener(v -> {
            facebookLogInButton.callOnClick();
        });


        // log in with firebase
        registerButton = findViewById(R.id.register_from_login_button);
        logInButton = findViewById(R.id.login_submit_button);

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        logInButton.setOnClickListener(v -> {
            preformFirebaseLogin();
        });
    }

    private void preformFirebaseLogin() {
        inputEmail = findViewById(R.id.login_email_text);
        inputPassword = findViewById(R.id.login_password_text);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        String emailText = inputEmail.getText().toString();
        String passwordText = inputPassword.getText().toString();


        if (!emailText.matches(validateEmailPattern)) {
            inputEmail.setError("Enter valid e-mail");
        } else if (passwordText.isEmpty() || passwordText.length() < 6) {
            inputPassword.setError("Enter proper password");
        } else {
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT)
                                .show();
                        getToMainActivity();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    private void getToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

// TODO: uncomment this! (do not delete this piece of code!)
    @Override
    protected void onStart() {
        // this function will activate if the user signed up with google before
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            getToMainActivity();
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // TODO: deprecated: check what to do
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            callbackManager.onActivityResult(requestCode, resultCode, data);
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