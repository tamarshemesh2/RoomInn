package postpc.finalproject.RoomInn.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView
import me.toptas.fancyshowcase.listener.OnCompleteListener
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.Furniture
import postpc.finalproject.RoomInn.furnitureData.Point3D
import postpc.finalproject.RoomInn.models.RoomInnApplication
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class FloorPlanFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    var furnitureList: List<FurnitureOnBoard> = listOf()
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var hamburger: ImageView
    lateinit var help: ImageButton

    val requestStoragePermissionLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                var saveImageFlag = true
                permissions.entries.forEach {
                    saveImageFlag = it.value
                }
                if (saveImageFlag) {
                    shareScreenShootResult()
                } else {
                    Toast.makeText(requireContext(), "can't share screenshot", Toast.LENGTH_LONG).show()
                }
            }

    val permissionListener: () -> Boolean = {
        if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requestStoragePermissionLauncher.launch(
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            )
            false
        }
    }

    companion object {
        fun newInstance() = FloorPlanFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor_plan, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set the view direction -> LTR
        this.activity?.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;

        // TODO: finish finding all the views
        // find all views
        val roomCanvas: FragmentContainerView = view.findViewById(R.id.floorPlan)
        val roomLayout: RelativeLayout = view.findViewById(R.id.floorPlanLayout)
        val roomTitle: TextView = view.findViewById(R.id.titleTextView)
        val addFab: ImageButton = view.findViewById(R.id.addButton)
        val playButton: FloatingActionButton = view.findViewById(R.id.playButton)
        var mQueue: FancyShowCaseQueue? = null

        var toAddFurniture = false
        //add all furniture to board

        // find hamburger views:
        drawerLayout = view.findViewById(R.id.draw_layout)
        navigationView = view.findViewById(R.id.hamburger_settings_navigation_layout)
        hamburger = view.findViewById(R.id.hamburgerMenuButton)

        help = view.findViewById(R.id.helpButton)



        if (RoomInnApplication.getInstance().getRoomsDB().user.firstRun){
            RoomInnApplication.getInstance().getRoomsDB().user.firstRun = false
            RoomInnApplication.getInstance().getRoomsDB().updateFirebase()
            val fancyShowCaseViewAdd = FancyShowCaseView.Builder(requireActivity())
                .focusOn(addFab)
                .title("tap to add furniture")
                .focusBorderColor(Color.GRAY)
                .focusBorderSize(10)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewPlay = FancyShowCaseView.Builder(requireActivity())
                .focusOn(playButton)
                .title("tap to view room in 3D")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewHelp = FancyShowCaseView.Builder(requireActivity())
                .focusOn(help)
                .title("tap to get help")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewMenu = FancyShowCaseView.Builder(requireActivity())
                .focusOn(hamburger)
                .title("tap to export your design")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewRoom = FancyShowCaseView.Builder(requireActivity())
                .focusOn(roomLayout)
                .title("tap furniture:\n1. once to rotate\n2. twice to edit\n")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .focusCircleRadiusFactor(.75)
                .build()
            val fancyShowCaseViewRoom2 = FancyShowCaseView.Builder(requireActivity())
                .focusOn(roomLayout)
                .title("pinch furniture to change size")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .focusCircleRadiusFactor(.75)
                .build()
            mQueue = FancyShowCaseQueue()
                .add(fancyShowCaseViewAdd)
                .add(fancyShowCaseViewPlay)
                .add(fancyShowCaseViewMenu)
                .add(fancyShowCaseViewHelp)
                .add(fancyShowCaseViewRoom)
                .add(fancyShowCaseViewRoom2)
            projectViewModel.helpMenuQueue = mQueue
            mQueue.show()
            mQueue.completeListener = object : OnCompleteListener {
                override fun onComplete() {
                    mQueue!!.cancel(true)
                }
            }
        }

        roomTitle.text = "${projectViewModel.projectName} \n- Floor Plan"

        playButton.setOnClickListener {
            RoomInnApplication.getInstance().getRoomsDB().saveRoom(projectViewModel.room)
            var intent = Intent(context, RoomUnityPlayerActivity::class.java)
            intent.putExtra("Scene Index", RoomUnityPlayerActivity.sceneIndex)
            intent.putExtra("User ID", projectViewModel.room.userId)
            intent.putExtra("Room Name", projectViewModel.room.name)
            intent.putExtra("Return To", 1)
            startActivity(intent)
            Log.d("updateRoom", "in flore plan, roomsMap is ${RoomInnApplication.getInstance().getRoomsDB().roomsMap}")
            }

        help.setOnClickListener {
            val fancyShowCaseViewAdd = FancyShowCaseView.Builder(requireActivity())
                .focusOn(addFab)
                .title("tap to add furniture")
                .focusBorderColor(Color.GRAY)
                .focusBorderSize(10)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewPlay = FancyShowCaseView.Builder(requireActivity())
                .focusOn(playButton)
                .title("tap to view room in 3D")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewHelp = FancyShowCaseView.Builder(requireActivity())
                .focusOn(help)
                .title("tap to get help")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewMenu = FancyShowCaseView.Builder(requireActivity())
                .focusOn(hamburger)
                .title("tap to export your design")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .build()
            val fancyShowCaseViewRoom = FancyShowCaseView.Builder(requireActivity())
                .focusOn(roomLayout)
                .title("tap furniture:\n1. once to rotate\n2. twice to edit\n")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .focusCircleRadiusFactor(.75)
                .build()
            val fancyShowCaseViewRoom2 = FancyShowCaseView.Builder(requireActivity())
                .focusOn(roomLayout)
                .title("pinch furniture to change size")
                .focusBorderColor(Color.GRAY)
                .titleStyle((R.style.MyTitleStyle), Gravity.CENTER)
                .enableAutoTextPosition()
                .focusBorderSize(10)
                .backgroundColor(0)
                .focusCircleRadiusFactor(.75)
                .build()
            mQueue = FancyShowCaseQueue()
                .add(fancyShowCaseViewAdd)
                .add(fancyShowCaseViewPlay)
                .add(fancyShowCaseViewMenu)
                .add(fancyShowCaseViewHelp)
                .add(fancyShowCaseViewRoom)
                .add(fancyShowCaseViewRoom2)
            projectViewModel.helpMenuQueue = mQueue
            mQueue!!.show()
            mQueue!!.completeListener = object : OnCompleteListener {
                override fun onComplete() {
                    mQueue!!.cancel(true)
                }
            }
        }


        val vto = roomLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                roomLayout.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)

                roomCanvas.setOnTouchListener { v, event ->
                    if (toAddFurniture) {
                        projectViewModel.currentPosition =
                            Point3D(event.rawX, 0f, event.rawY).toAbsolutLocation(
                                projectViewModel.room.getRoomRatio(),
                                projectViewModel.layoutMeasures
                            )
                        projectViewModel.newFurniture = true
                        Navigation.findNavController(v)
                            .navigate(R.id.action_floorPlanFragment_to_addFurnitureFragment2)
                        toAddFurniture = false
                    }
                    return@setOnTouchListener true
                }
                addFab.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Tap where you wish to place a new furniture",
                        Toast.LENGTH_SHORT
                    ).show()
                    toAddFurniture = true
                }
            }
        })
        hamburger.setOnClickListener {
        if (! drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
            navigationView.bringToFront()
            navigationView.setNavigationItemSelectedListener(this)
        }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_floor_plan_img) {
            shareScreenShootResult()
        } else if (item.itemId == R.id.share_item_list) {
            shareLIstOfFurniture()
        }
        return true
    }

    fun shareLIstOfFurniture() {
        val roomName = projectViewModel.room.name
        val furList = RoomInnApplication.getInstance().getRoomsDB().roomFurniture(roomName)
        val message = getStringToShare(furList, roomName)
        if (furList.isEmpty()) {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        } else {
            shareText(message)
        }


    }

    fun shareText(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    fun getStringToShare(furList: MutableList<Furniture>, roomName: String): String {
        var text = "Here are the items I chose for '$roomName' project:\n\n"
        furList.forEach {
            text += "${it.toExportString()}\n"
        }
        if (furList.isEmpty()) {
            text = "You did not place any furniture in room $roomName"
        }
        return text
    }

    private fun shareScreenShootResult() {
        val dateFormatter by lazy {
            SimpleDateFormat(
                    "yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault()
            )
        }
        Log.d("app name:", requireContext().applicationInfo.name)
        val filename = "${getString(R.string.my_ScreenShoot)}${dateFormatter.format(Date())}.png"
        val ScreenShootFolderPath = File.separator + requireContext().getAppName()

        val layout: View? = view?.findViewById(R.id.room_layout)
        val uri = layout?.makeScreenShot()
                ?.saveScreenShot(requireContext(), filename, ScreenShootFolderPath, permissionListener)
                ?: return

        dispatchShareImageIntent(uri)
    }

    private fun dispatchShareImageIntent(screenShotUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, screenShotUri)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun Context.getAppName(): String {
        var appName: String = ""
        val applicationInfo = applicationInfo
        val stringId = applicationInfo.labelRes
        appName = if (stringId == 0) {
            applicationInfo.nonLocalizedLabel.toString()
        } else {
            getString(stringId)
        }
        return appName
    }

    private fun View.makeScreenShot(): Bitmap {
        setBackgroundColor(Color.WHITE)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    private fun Bitmap.saveScreenShot(
            requireContext: Context,
            filename: String,
            ScreenShootFolderPath: String,
            permissionListener: () -> Boolean,
    ): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            saveImageInQ(this, filename, ScreenShootFolderPath, requireContext.contentResolver)
        else
            legacySave(this, filename, ScreenShootFolderPath, permissionListener)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(
            bitmap: Bitmap,
            filename: String,
            parentFileName: String,
            contentResolver: ContentResolver
    ): Uri? {
        val fos: OutputStream?
        val uri: Uri?
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.Files.FileColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + parentFileName)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let { contentResolver.openOutputStream(it) }.also { fos = it }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        fos?.flush()
        fos?.close()

        contentValues.clear()
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        uri?.let {
            contentResolver.update(it, contentValues, null, null)
        }
        return uri
    }

    private fun legacySave(
            bitmap: Bitmap,
            filename: String,
            parentFileName: String,
            permissionListener: () -> Boolean,
    ): Uri? {
        val fos: OutputStream?
        if (!permissionListener()) {
            return null
        }

        val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +
                        parentFileName + File.separator + filename
        val imageFile = File(path)
        if (imageFile.parentFile?.exists() == false) {
            imageFile.parentFile?.mkdir()
        }
        imageFile.createNewFile()
        fos = FileOutputStream(imageFile)
        val uri: Uri = Uri.fromFile(imageFile)

        fos.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        fos.flush()
        fos.close()

        return uri
    }
}