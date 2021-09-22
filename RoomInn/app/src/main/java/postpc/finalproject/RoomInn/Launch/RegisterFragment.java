package postpc.finalproject.RoomInn.Launch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;

public class RegisterFragment extends Fragment {
    String validateEmailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

    EditText inputEmail, inputPassword, repeatPassword;
    Button registerButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public RegisterFragment() {
        super(R.layout.register_fregment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEmail = view.findViewById(R.id.register_edit_text);
        inputPassword = view.findViewById(R.id.register_password_text);
        repeatPassword = view.findViewById(R.id.register_re_enter_password_text);
        registerButton = view.findViewById(R.id.register_submit_button);
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        registerButton.setOnClickListener(v -> {
            preformAuth();
        });
    }


    private void preformAuth() {
        String emailText = inputEmail.getText().toString();
        String passwordText = inputPassword.getText().toString();
        String repeatPasswordText = repeatPassword.getText().toString();

        if (!emailText.matches(validateEmailPattern)) {
            inputEmail.setError("Enter valid e-mail");
        } else if (passwordText.isEmpty() || passwordText.length() < 6) {
            inputPassword.setError("Enter proper password");
        } else if (!passwordText.equals(repeatPasswordText)) {
            repeatPassword.setError("password dose not match");
        } else {
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "registration successful", Toast.LENGTH_SHORT)
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
}
