package postpc.finalproject.RoomInn

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import postpc.finalproject.RoomInn.models.RoomInnApplication.Companion.getInstance
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import postpc.finalproject.RoomInn.models.LoadingStage




class MainActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    var viewModel: ProjectViewModel? = null




    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        //sets the main activity to portrait orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;

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
        viewModel = ViewModelProvider(this).get(
            ProjectViewModel::class.java
        )
        viewModel!!.activityContext = this
        listenToLoadingStage()

        val intent = intent
        if (intent.extras != null) {
            val userId = intent.getStringExtra("User ID")
            val roomName = intent.getStringExtra("Room Name")
            RoomInnApplication.getInstance().pathToUnity =
                intent.getStringExtra("Path To Unity") ?: ""
            viewModel!!.goTo = intent.getIntExtra("Return To", 0)

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            getInstance().getRoomsDB()
                .initializeAfterUnity(userId!!, roomName!!, navController, viewModel)
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
        super.onSaveInstanceState(outState)
        val DB = getInstance().getRoomsDB()
        DB.saveOnExit()
    }

    override fun onDestroy() {
        val DB = getInstance().getRoomsDB()
        DB.rooms.removeObservers(this)
        DB.userLoadingStage.removeObservers(this)
        DB.saveOnExit()
        super.onDestroy()
    }

    override fun onPause() {
        val DB = getInstance().getRoomsDB()
        DB.saveOnExit()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        val DB = getInstance().getRoomsDB()
        DB.saveOnExit()
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onBackPressed() {
        if (viewModel != null && viewModel!!.helpMenuQueue != null) {
            viewModel!!.helpMenuQueue?.cancel(true)
        }
        super.onBackPressed()
    }
}