package postpc.finalproject.RoomInn.launch

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance

class LoginFragment : Fragment(R.layout.login_fegment) {
    var RC_SIGN_IN = 0
    var validateEmailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var facebookLogInButton: LoginButton? = null
    lateinit var registerButton: Button
    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var logInButton: Button
    lateinit var progressDialog: ProgressDialog
    lateinit var mAuth: FirebaseAuth
    private var viewModel: LoginViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        viewModel!!.changeTitle("LogInn")

        // setup login with google:
        val googleLogInButton: SignInButton = view.findViewById(R.id.google_log_in_button)
        googleLogInButton.isEnabled = false
        googleLogInButton.visibility = View.GONE
        val googleViewButton = view.findViewById<ImageView>(R.id.google_image)
        googleViewButton.isEnabled = true
        googleViewButton.visibility = View.VISIBLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleLogInButton.setOnClickListener { v: View ->
            if (v.id == R.id.google_log_in_button) {
                viewModel!!.googleSingInCallable()
            }
        }
        googleViewButton.setOnClickListener { v: View? ->
            googleLogInButton.callOnClick()
            viewModel!!.googleSingInCallable()
        }

        // setup login with facebook:
        facebookLogInButton = view.findViewById(R.id.facebook_log_in_button)
        facebookLogInButton!!.setEnabled(false)
        facebookLogInButton!!.setVisibility(View.GONE)
        val facebookViewButton = view.findViewById<ImageView>(R.id.facebook_image)
        facebookViewButton.isEnabled = true
        facebookViewButton.visibility = View.VISIBLE


        facebookLogInButton!!.registerCallback(viewModel!!.callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                goToMainActivity()
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }
        })
        facebookViewButton.setOnClickListener { v: View? -> facebookLogInButton!!.callOnClick() }


        // log in with firebase
        registerButton = view.findViewById(R.id.register_from_login_button)
        logInButton = view.findViewById(R.id.login_submit_button)
        registerButton.setOnClickListener(View.OnClickListener { v: View? ->
            viewModel!!.isRegistering = true
            val activity = activity
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(activity.findViewById<View>(R.id.fragment_frame).id, viewModel!!.registerFragment)
                    .commit()
        })
        logInButton.setOnClickListener(View.OnClickListener { v: View? -> preformFirebaseLogin(view) })
    }

    private fun preformFirebaseLogin(view: View) {
        inputEmail = view.findViewById(R.id.login_email_text)
        inputPassword = view.findViewById(R.id.login_password_text)
        progressDialog = ProgressDialog(activity)
        mAuth = FirebaseAuth.getInstance()
        val emailText = inputEmail.getText().toString()
        val passwordText = inputPassword.getText().toString()
        if (!emailText.matches(validateEmailPattern)) {
            inputEmail.setError("Enter valid e-mail")
        } else if (passwordText.isEmpty() || passwordText.length < 6) {
            inputPassword.setError("Enter proper password")
        } else {
            progressDialog.setTitle("Login")
            progressDialog.setMessage("Please wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT)
                            .show()
                    goToMainActivity()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "" + task.exception, Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    private fun goToMainActivity() {
            getInstance().getRoomsDB().initialize(userID())
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    // user  is already logged in with facebook
    private fun userID(): String {
            var userId: String? = null
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (acct != null) {
                userId = acct.id
                Log.d("login", "login with google id: $userId")
            }

            // user  is already logged in with facebook
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                userId = accessToken!!.userId
            }
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                userId = user.uid
            }
            return userId!!
        }
}