// TODO: delete this file

package postpc.finalproject.RoomInn.Launch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import postpc.finalproject.RoomInn.MainActivity;
import postpc.finalproject.RoomInn.R;

public class RegisterActivity extends AppCompatActivity {

    String validateEmailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

    EditText inputEmail, inputPassword, repeatPassword;
    Button registerButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputEmail = findViewById(R.id.register_edit_text);
        inputPassword = findViewById(R.id.register_password_text);
        repeatPassword = findViewById(R.id.register_re_enter_password_text);
        registerButton = findViewById(R.id.register_submit_button);
        progressDialog = new ProgressDialog(this);
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
                            Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_SHORT)
                                    .show();
                            getToMainActivity();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT)
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
}
