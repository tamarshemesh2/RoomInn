package postpc.finalproject.RoomInn.ViewModle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import postpc.finalproject.RoomInn.Launch.LoginFragment
import postpc.finalproject.RoomInn.Launch.RegisterFragment

class LoginViewModel : ViewModel(){
    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    val loginFragment: Fragment? = LoginFragment()
    val registerFragment: Fragment = RegisterFragment()
}