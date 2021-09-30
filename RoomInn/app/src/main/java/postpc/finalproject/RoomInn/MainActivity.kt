package postpc.finalproject.RoomInn

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import postpc.finalproject.RoomInn.models.LoadingStage
import postpc.finalproject.RoomInn.models.RoomsDB

class MainActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentContainer =
            this.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
        progressDialog = ProgressDialog(this)
        if (savedInstanceState != null) {
            val userId = savedInstanceState.getString("userId", null)
            if (userId != null) {
                getInstance().getRoomsDB().initialize(userId)
            }
        }
        val viewModel = ViewModelProvider(this).get(
            ProjectViewModel::class.java
        )
        viewModel.activityContext = this
        listenToLoadingStage()

        // todo - add the function of tamar, initialize by user id and room name
        val intent = intent
        if (intent.extras != null) {
            val userId = intent.getStringExtra("User ID")
            val roomName = intent.getStringExtra("Room Name")
            viewModel.goTo = intent.getIntExtra("Return To",0)
            // 0 = profileFragment ,
            // 1 = floorPlanFragment ,
            // 2 = rotateFloorPlanFragment


            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            getInstance().getRoomsDB().initializeAfterUnity(userId!!, roomName!!, navController, viewModel)


        }
    }

    private fun listenToLoadingStage() {
        getInstance().getRoomsDB().userLoadingStage.observe(
            this,
            { loadingStage ->
                if (loadingStage === LoadingStage.LOADING) {
                    progressDialog!!.setTitle("Fetching Project")
                    progressDialog!!.setMessage("Please wait...")
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.show()
                } else {
                    progressDialog!!.dismiss()
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("main activity", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        val DB = getInstance().getRoomsDB()
        DB.saveOnExit()
    }

    override fun onDestroy() {
        Log.d("main activity", "onDestroy")
        val DB = getInstance().getRoomsDB()
        DB.rooms.removeObservers(this)
        DB.userLoadingStage.removeObservers(this)
        DB.saveOnExit()
        super.onDestroy()
    }

    override fun onPause() {
        Log.d("main activity", "onPause")
        val DB = getInstance().getRoomsDB()
        DB.saveOnExit()
        super.onPause()
    }
}