package postpc.finalproject.RoomInn.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.AccessToken
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.launch.LaunchActivity
import postpc.finalproject.RoomInn.models.RoomInnApplication
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
        private const val CAMERA_PERMISSION_CODE = 100
        const val STORAGE_PERMISSION_CODE = 101
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("createdProfile", "==================================================")

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find all views
        val projectRecyclerView: RecyclerView = view.findViewById(R.id.projects_recycler)
        val addProjectFab: FloatingActionButton = view.findViewById(R.id.fab_add)
        val singOut: ImageButton = view.findViewById(R.id.sing_out)
        val adapter = projectViewModel.adapter
        adapter.setContext(requireContext())

        // setup function to call from DB upon change the current rendered room
        RoomInnApplication.getInstance().getRoomsDB().loadRoomNavLambda = {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment2_to_floorPlanFragment)
        }
        // needs to be changed to daniella's unity features
        addProjectFab.setOnClickListener {
            checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE
            )
            checkPermission(android.Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
            val intent = Intent(requireContext(), ScanUnityHandler::class.java)
            intent.putExtra("User ID", RoomInnApplication.getInstance().getRoomsDB().user.id)
            intent.putExtra("Return To", 2)
            projectViewModel.doorsAndWindows.clear()
            startActivity(intent)
//            Navigation.findNavController(view)
//                .navigate(R.id.action_profileFragment2_to_floorPlanRotateFragment)
        }

        singOut.setOnClickListener {
            singedOut()
            var intent = Intent(requireActivity(), LaunchActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // set the recycle view
        projectRecyclerView.adapter = adapter
        projectRecyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 1, RecyclerView.VERTICAL, false)
        projectRecyclerView.layoutManager
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    fun singedOut() {
        // if facebook
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)

        //if google
        GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()

        // if firebase
        FirebaseAuth.getInstance().signOut()
    }

    // Function to check and request permission.
    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Camera Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
