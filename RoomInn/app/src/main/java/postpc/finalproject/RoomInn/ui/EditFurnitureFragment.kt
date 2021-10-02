package postpc.finalproject.RoomInn.ui


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import postpc.finalproject.RoomInn.FurnitureCanvas
import postpc.finalproject.RoomInn.R
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.models.RoomInnApplication
import postpc.finalproject.RoomInn.models.RoomsDB


class EditFurnitureFragment : Fragment() {
    companion object {
        fun newInstance() = EditFurnitureFragment()
    }

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProjectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun renderDrawing(furniture: Furniture, furnitureImageView: FurnitureCanvas) {
        val sizeToDraw = furniture.getSizeToDraw(
            Size(
                furnitureImageView.height - 20,
                furnitureImageView.height - 20
            )
        )
        if (furniture.type == "Window") {
            furnitureImageView.setPath(
                (furniture as Window).drawFront(
                    sizeToDraw.first,
                    sizeToDraw.second
                )
            )
        } else {
            furnitureImageView.setPath(
                furniture.draw(
                    sizeToDraw.first,
                    sizeToDraw.second
                )
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;

        // find all the views

        val colorBtn: ImageView = view.findViewById(R.id.color_btn)
        val colorTxt = view.findViewById<TextView>(R.id.color_text)
        val widthEditText = view.findViewById<EditText>(R.id.width_edit_text)
        val heightEditText = view.findViewById<EditText>(R.id.height_edit_text)
        val lengthEditText = view.findViewById<EditText>(R.id.length_edit_text)
        val rotateEditText = view.findViewById<EditText>(R.id.rotate_inner_text)
        val rotateBtn = view.findViewById<ImageButton>(R.id.rotate_btn)

        val freeRatioCheckBox = view.findViewById<CheckBox>(R.id.enable_ratio_checkbox)
        val furnitureCanvas = view.findViewById<FurnitureCanvas>(R.id.furniture_img)
        val saveFab = view.findViewById<FloatingActionButton>(R.id.save_fab)
        val delFab = view.findViewById<FloatingActionButton>(R.id.delete_fab)
        val furnitureImageView = view.findViewById<ImageView>(R.id.furniture_render_img)


        //TODO: support the delete button

        val furniture = projectViewModel.furniture!!
        val DB: RoomsDB = RoomInnApplication.getInstance().getRoomsDB()


        val vto = furnitureCanvas.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                furnitureCanvas.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                renderDrawing(furniture, furnitureCanvas)
            }
        })
        furnitureCanvas.rotation = furniture.rotation.y
        val typeMap: Map<Int, FurnitureType>? = (when (furniture.type) {
            ("Chair") -> Chair.typeMap
            ("Armchair") -> Armchair.typeMap
            ("Bed") -> Bed.typeMap
            ("Closet") -> Closet.typeMap
            ("Couch") -> Couch.typeMap
            ("Table") -> Table.typeMap
            ("Dresser") -> Dresser.typeMap
            ("Door")->Door.typeMap
            else -> null
        })
        if (typeMap != null) {
            furnitureImageView.setImageResource(typeMap[furniture.unityType.key]!!.typeRecID)
            furnitureImageView.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_editFurnitureFragment_to_chooseFurnitureTypeFragment)
            }
            if (furniture.type != "Door"){
            colorTxt.setTextColor(Color.GRAY)
            colorBtn.visibility = View.VISIBLE}
            else{
                colorBtn.visibility = View.GONE
                colorTxt.setTextColor(Color.TRANSPARENT)
            }

        }else{
            colorBtn.visibility = View.GONE
            colorTxt.setTextColor(Color.TRANSPARENT)
        }

        colorBtn.setColorFilter(furniture.color)
        widthEditText.setText(furniture.scale.x.toString())
        lengthEditText.setText(furniture.scale.z.toString())
        if (furniture.type == "Window") {
            heightEditText.setText(furniture.position.y.toString())
        } else {
            heightEditText.setText(furniture.scale.y.toString())
        }
        rotateEditText.setText(furniture.rotation.y.toString())
        widthEditText.isEnabled = furniture.freeScale
        lengthEditText.isEnabled = furniture.freeScale
        heightEditText.isEnabled = furniture.freeScale
        rotateEditText.isEnabled = furniture.freeScale
        freeRatioCheckBox.isChecked = furniture.freeScale

        colorBtn.setOnClickListener { v ->

            val colorPickerDialog: ColorPickerDialog =
                ColorPickerDialog.createColorPickerDialog(
                    requireContext(),
                    ColorPickerDialog.LIGHT_THEME
                )
            colorPickerDialog.setOnColorPickedListener { color, _ ->
                //Your code here
                colorBtn.setColorFilter(color)
                furniture.color = color
            }
            colorPickerDialog.hideOpacityBar()
            colorPickerDialog.show();
        }

        freeRatioCheckBox.setOnClickListener {
            val bool = freeRatioCheckBox.isChecked
            widthEditText.isEnabled = bool
            lengthEditText.isEnabled = bool
            heightEditText.isEnabled = bool
            rotateEditText.isEnabled = bool
            furniture.freeScale = bool
        }

        rotateBtn.setOnClickListener {
            furniture.rotation.y = (furniture.rotation.y + 45) % 360
            rotateEditText.setText(furniture.rotation.y.toString())
            furnitureCanvas.rotation = furniture.rotation.y
        }


        rotateEditText.doOnTextChanged { txt, _, _, _ ->
            var num = txt.toString()
            if (num == "") {
                num = "0"
            }
            furniture.rotation.y = num.toFloat() % 360
            furnitureCanvas.rotation = furniture.rotation.y
        }

        delFab.setOnClickListener {
            DB.deleteFurniture(projectViewModel.furniture!!)

            if (furniture.type in listOf("Door", "Window") || !projectViewModel.newFurniture) {
                Navigation.findNavController(it).popBackStack()
            } else {
                Navigation.findNavController(it)
                    .navigate(R.id.action_editFurnitureFragment_to_floorPlanFragment)
            }

        }

        saveFab.setOnClickListener {
            if (lengthEditText.text.isEmpty()) {
                lengthEditText.setText("0")
            }
            if (heightEditText.text.isEmpty()) {
                heightEditText.setText("0")
            }
            if (widthEditText.text.isEmpty()) {
                widthEditText.setText("0")
            }

            if (furniture.type == "Window") {
                furniture.position.y = lengthEditText.text.toString().toFloat()
            } else {
                furniture.scale.y = heightEditText.text.toString().toFloat()
            }
            furniture.scale.z = lengthEditText.text.toString().toFloat()
            furniture.scale.x = widthEditText.text.toString().toFloat()
            projectViewModel.furniture = furniture

            if (furniture.type !in listOf("Door", "Window")) {
                // update the furniture in the DB only if it is a furniture
                DB.furnitureMap[projectViewModel.furniture!!.id] = projectViewModel.furniture!!
                if (projectViewModel.furniture!!.id !in DB.roomToFurnitureMap[projectViewModel.room.id]!!) {
                    DB.roomToFurnitureMap[projectViewModel.room.id]!!.add(projectViewModel.furniture!!.id)
                }

                Navigation.findNavController(it)
                        .navigate(R.id.action_editFurnitureFragment_to_floorPlanFragment)
            }
            else {
                Navigation.findNavController(it)
                    .navigate(R.id.action_editFurnitureFragment_to_floorPlanPlacingFragment)
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_furniture, container, false)
    }

}
