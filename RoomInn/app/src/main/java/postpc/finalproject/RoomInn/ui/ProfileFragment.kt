package postpc.finalproject.RoomInn.ui

import android.content.Intent
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
import java.io.File

class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // check if returned from scan
        lookForScan(view)
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
            val intent = Intent(requireContext(), ScanUnityHandler::class.java)
            intent.putExtra("User ID", RoomInnApplication.getInstance().getRoomsDB().user.id)
            startActivity(intent)
            projectViewModel.doorsAndWindows.clear()
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
    private fun lookForScan(view: View) {
        val value = RoomInnApplication.getInstance().pathToUnity.value
        if (value != "") {
            val cornersFileName = value + projectViewModel.pointsPathName
            val distancesFileName = value + projectViewModel.distancesPathName
            val cornersFile = File(cornersFileName)
            val distFile = File(distancesFileName)
            val nowMillis = System.currentTimeMillis()
            val lastModifiedThreshold = 1000
            if (cornersFile.exists() && distFile.exists() &&
                ((cornersFile.lastModified() - nowMillis < lastModifiedThreshold) ||
                 (distFile.lastModified() - nowMillis < lastModifiedThreshold))) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment2_to_floorPlanRotateFragment)
            }
        }
    }

}
