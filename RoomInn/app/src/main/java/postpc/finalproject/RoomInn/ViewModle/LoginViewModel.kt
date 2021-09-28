package postpc.finalproject.RoomInn.ViewModle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import postpc.finalproject.RoomInn.launch.LoginFragment
import postpc.finalproject.RoomInn.launch.RegisterFragment

class LoginViewModel : ViewModel(){
    var isRegistering: Boolean = false
    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    val loginFragment: Fragment = LoginFragment()
    val registerFragment: Fragment = RegisterFragment()
    lateinit var googleSingInCallable: () -> Unit
    lateinit var changeTitle: (String) -> Unit
}