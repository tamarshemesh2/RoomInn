package postpc.finalproject.RoomInn.launch

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import postpc.finalproject.RoomInn.MainActivity
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.LoginViewModel
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance

class RegisterFragment : Fragment(R.layout.register_fregment) {
    var validateEmailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var repeatPassword: EditText
    lateinit var registerButton: Button
    lateinit var progressDialog: ProgressDialog
    lateinit var mAuth: FirebaseAuth
    private var viewModel: LoginViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        viewModel!!.changeTitle("Register")

        inputEmail = view.findViewById(R.id.register_edit_text)
        inputPassword = view.findViewById(R.id.register_password_text)
        repeatPassword = view.findViewById(R.id.register_re_enter_password_text)
        registerButton = view.findViewById(R.id.register_submit_button)
        progressDialog = ProgressDialog(activity)
        mAuth = FirebaseAuth.getInstance()
        registerButton.setOnClickListener { v: View? -> preformAuth() }
    }

    private fun preformAuth() {
        val emailText = inputEmail.text.toString()
        val passwordText = inputPassword.text.toString()
        val repeatPasswordText = repeatPassword.text.toString()
        if (!emailText.matches(validateEmailPattern)) {
            inputEmail.error = "Enter valid e-mail"
        } else if (passwordText.isEmpty() || passwordText.length < 6) {
            inputPassword.error = "Enter proper password"
        } else if (passwordText != repeatPasswordText) {
            repeatPassword.error = "password dose not match"
        } else {
            progressDialog.setTitle("Registration")
            progressDialog.setMessage("Please wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(activity, "registration successful", Toast.LENGTH_SHORT)
                                    .show()
                            toMainActivity()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(activity, "" + task.exception, Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
        }
    }

    private fun toMainActivity() {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            getInstance().getRoomsDB().initialize(userId)
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
}