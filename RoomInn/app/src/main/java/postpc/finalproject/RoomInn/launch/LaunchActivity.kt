package postpc.finalproject.RoomInn.launch

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance

class LaunchActivity : AppCompatActivity() {
    var RC_SIGN_IN = 0
    private var progressDialog: ProgressDialog? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit private var titleText: TextView

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_launch)
        titleText = findViewById(R.id.MyProjects_title)

        viewModel.googleSingInCallable =  { googleSignIn() }
        viewModel.changeTitle = { titleText.text = it }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        val fragment = findViewById<FragmentContainerView>(R.id.fragment_frame)
        if (viewModel.isRegistering) {
            supportFragmentManager.beginTransaction()
                    .replace(fragment.id, viewModel.registerFragment)
                    .commit()
        } else {
            supportFragmentManager.beginTransaction()
                    .replace(fragment.id, viewModel.loginFragment)
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        val userId = userID()
        if (userId != null) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setTitle("Login")
            progressDialog!!.setMessage("Please wait")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                    .addOnSuccessListener { v: DocumentSnapshot ->
                        if (v.exists()) {
                            getToMainActivity(userId)
                            progressDialog!!.dismiss()
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT)
                                    .show()
                        } else {
                            FirebaseAuth.getInstance().signOut()
                            GoogleSignIn.getClient(applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                            progressDialog!!.dismiss()
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                    .addOnFailureListener { v: Exception? ->
                        FirebaseAuth.getInstance().signOut()
                        GoogleSignIn.getClient(applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                        progressDialog!!.dismiss()
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT)
                                .show()
                    }
        } else {
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun getToMainActivity(userId: String) {
        getInstance().getRoomsDB().initialize(userId)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            getToMainActivity(account.id!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
        }
    }

    // user  is already logged in with facebook
    private fun userID(): String? {
            var userId: String? = null
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                userId = acct.id
                Log.d("login", "login with google id: $userId")
            }

            // user  is already logged in with facebook
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                userId = accessToken!!.userId
                Log.d("login", "login with facebook id: $userId")
            }
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                userId = user.uid
                Log.d("login", "login with firebase id: $userId")
            }
            return userId
        }

    override fun onBackPressed() {
        if (viewModel.isRegistering) {
            viewModel.isRegistering = false
            val ft = supportFragmentManager.beginTransaction()
                    .replace(findViewById<View>(R.id.fragment_frame).id, viewModel.loginFragment)
            ft.commit()
        } else {
            super.onBackPressed()
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        signInIntent.putExtra("rc_code", RC_SIGN_IN)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}