package postpc.finalproject.RoomInn.Launch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel;

public class LoginFragment extends Fragment {

    int RC_SIGN_IN = 0;
    String validateEmailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton facebookLogInButton;
    Button registerButton;
    EditText inputEmail;
    EditText inputPassword;
    Button logInButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private LoginViewModel viewModel;

    public LoginFragment() {
        super(R.layout.login_fegment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // user  is already logged in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            getToMainActivity();
        }

        // setup login with google:
        SignInButton googleLogInButton = view.findViewById(R.id.google_log_in_button);
        googleLogInButton.setEnabled(false);
        googleLogInButton.setVisibility(View.GONE);

        ImageView googleViewButton = view.findViewById(R.id.google_image);
        googleViewButton.setEnabled(true);
        googleViewButton.setVisibility(View.VISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleLogInButton.setOnClickListener(v -> {
            if (v.getId() == R.id.google_log_in_button) {
                googleSignIn();
            }
        });

        googleViewButton.setOnClickListener(v -> {
            googleLogInButton.callOnClick();
        });


        // setup login with facebook:
        facebookLogInButton = view.findViewById(R.id.facebook_log_in_button);
        facebookLogInButton.setEnabled(false);
        facebookLogInButton.setVisibility(View.GONE);

        ImageView facebookViewButton = view.findViewById(R.id.facebook_image);
        facebookViewButton.setEnabled(true);
        facebookViewButton.setVisibility(View.VISIBLE);


//        facebookLogInButton.setPermissions(Arrays.asList(...));
        facebookLogInButton.registerCallback(viewModel.getCallbackManager(), new FacebookCallback<LoginResult>() {
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

        registerButton = view.findViewById(R.id.register_from_login_button);
        logInButton = view.findViewById(R.id.login_submit_button);
        registerButton.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(activity.findViewById(R.id.fragment_frame).getId(), viewModel.getRegisterFragment())
                    .commit();
        });

        logInButton.setOnClickListener(v -> {
            preformFirebaseLogin(view);
        });
    }


    private void preformFirebaseLogin(@NonNull View view) {
        inputEmail = view.findViewById(R.id.login_email_text);
        inputPassword = view.findViewById(R.id.login_password_text);
        progressDialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT)
                                .show();
                        getToMainActivity();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    private void getToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // TODO: deprecated: check what to do
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}