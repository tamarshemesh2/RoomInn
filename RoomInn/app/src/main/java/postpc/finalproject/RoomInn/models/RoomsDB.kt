package postpc.finalproject.RoomInn.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import postpc.finalproject.RoomInn.Room
import postpc.finalproject.RoomInn.ViewModle.ProjectViewModel
import postpc.finalproject.RoomInn.furnitureData.*

class RoomsDB(val context: Context) {
    private var firebase: FirebaseFirestore= FirebaseFirestore.getInstance()
    lateinit var user: User
    private var isInitialized: Boolean = false
    lateinit var rooms: MutableLiveData<MutableList<String>>  // live data fo the rooms, containing copy of the users roomsSet
    var userLoadingStage: MutableLiveData<LoadingStage> = MutableLiveData<LoadingStage>(LoadingStage.SUCCESS)
    var roomsListenerLambda: () -> Unit = {}
    var loadRoomNavLambda: () -> Unit = {}
    var roomsMap: MutableMap<String, Room> = mutableMapOf()
    var furnitureMap: MutableMap<String, Furniture> = mutableMapOf()
    var roomToFurnitureMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    var isRoomLoaded : Boolean = false
    var areFurnitureLoaded : Boolean = false

    fun isInitialized() : Boolean {
        return isInitialized
    }

    fun createNewUser(id: String) {
        FirebaseApp.initializeApp(context)
        userLoadingStage.value = LoadingStage.LOADING
        val document = firebase.collection("users")
                .document(id)

        // TODO: remove this lines:
        user.roomsList = mutableListOf("project 1", "project 2", "project 3")
        val room1 = Room()
        room1.Corners = mutableListOf(
                Point3D(5f, 5f, 5f),
                Point3D(287.5f, 5f, 5f),
                Point3D(287.5f, 5f, 152f),
                Point3D(5f, 5f, 152f)
        )
        room1.name = "project 1"
        val room2 = Room()
        room2.Corners = mutableListOf(
                Point3D(-0.015324175357818604f,-0.8654714822769165f,1.9097247123718262f).multiply(100f),
                Point3D(0.8313037157058716f,-1.3702747821807862f,-0.5127741098403931f).multiply(100f),
                Point3D(-2.139707326889038f,-0.8447096347808838f,-1.2366341352462769f).multiply(100f),
                Point3D(-2.084784984588623f,-0.5958563089370728f,1.5056521892547608f).multiply(100f)
        )
        room2.name = "project 2"
        val room3 = Room()
        room3.Corners = mutableListOf(
                Point3D(5f, 5f, 5f),
                Point3D(200f, 5f, 5f),
                Point3D(200f, 5f, 152f),
                Point3D(5f, 5f, 152f)
        )
        room3.name = "project 3"
        room3.userId = user.id
        room2.userId = user.id
        room1.userId = user.id
        createNewRoom(room1)
        createNewRoom(room2)
        createNewRoom(room3)
        Log.d("Rooms List: ", "list form DB is ${rooms.value}.")
        // TODO: test for adding furniture! with yuval - add a furnitur manualy and checks that it's there

        // TODO: end of section to delete


        document.set(user)
                .addOnSuccessListener {
                    isInitialized = true
                    Log.d("Firebase", "loading user data succeeded")
                    userLoadingStage.value = LoadingStage.SUCCESS
                }
                .addOnFailureListener {
                    userLoadingStage.value = LoadingStage.FAILURE
                    Log.d("Firebase", "loading user data failed")
                }
                .addOnCanceledListener {
                    userLoadingStage.value = LoadingStage.SUCCESS
                    Log.d("Firebase", "loading user data failed")
                }
    }

    fun initialize(id: String) {

        FirebaseApp.initializeApp(context)
        userLoadingStage.value = LoadingStage.LOADING
        var document = firebase.collection("users")
                .document(id)
        document.get().addOnSuccessListener { d: DocumentSnapshot ->
            if (d.exists()) {
                user = d.toObject(User::class.java)!!
                rooms = MutableLiveData(user.roomsList)
                isInitialized = true
                userLoadingStage.value = LoadingStage.SUCCESS
            } else {
                user = User(id=id)
                rooms = MutableLiveData(user.roomsList)
                createNewUser(id)
                isInitialized = true
            }
            setRoomsListListener()
        }
                .addOnFailureListener {
                    userLoadingStage.value = LoadingStage.FAILURE
                    Log.d("Firebase", "loading user data failed")
                }
                .addOnCanceledListener {
                    userLoadingStage.value = LoadingStage.SUCCESS
                    Log.d("Firebase", "loading user data failed")
                }

    }

    fun updateRoom(room: Room) {
        firebase.collection("rooms").document(room.id).set(room)
    }

    fun createNewRoom(room: Room) {
        updateRoom(room)
        rooms.value!!.add(room.name)
        Log.d("Rooms List: ", "list form createRoom is ${rooms.value}.")
    }

    fun setRoomsListListener() {
        rooms.observeForever {
            user.roomsList = it
            roomsListenerLambda()
            firebase.collection("users").document(user.id).set(user)
        }
    }

//    fun setFurnitureListListener(viewModel : ProjectViewModel) {
//        roomToFurnitureMap[viewModel.room.id]!!.observeForever {
//            viewModel.room.furniture = it
//            // todo: update some lambda??
//            firebase.collection("rooms").document(viewModel.room.id).set(viewModel.room)
//        }
//    }

    /**
     * This function loade the roon into the room map, and updates the view model to hold the current room.
     */
    fun loadRoomByName(roomName: String, viewModel: ProjectViewModel ?= null) {

        userLoadingStage.value = LoadingStage.LOADING
        if (roomName in roomsMap) {
            if (viewModel != null) {
                viewModel.room = roomsMap[roomName]!!
            }
            loadRoomNavLambda()
            userLoadingStage.value = LoadingStage.SUCCESS
            return

        }
        firebase.collection("rooms").whereEqualTo("userId", user.id).whereEqualTo("name", roomName).get()
                .addOnSuccessListener {
                    val documents = it.documents
                    var room: Room? = null
                    for (doc in documents) {
                        room = doc.toObject(Room::class.java)
                        if (room != null) {
                            roomsMap[roomName] = room
                            addFurnitureToMap(room)
                        }
                    }
                    if (viewModel != null && room != null) {
                        viewModel.room = roomsMap[roomName]!!
                    }

                    // change to success only if both room and it's furniture are loaded
                    if (userLoadingStage.value == LoadingStage.LOADING) {
                        isRoomLoaded = true
                        if (areFurnitureLoaded) {
                            loadRoomNavLambda()
                            userLoadingStage.value = LoadingStage.SUCCESS
                            areFurnitureLoaded = false
                            isRoomLoaded = false
                        }
                    }
                }
                .addOnFailureListener {
                    userLoadingStage.value = LoadingStage.FAILURE
                }
    }

    private fun addFurnitureToMap(room: Room, viewModel: ProjectViewModel?= null) {
        roomToFurnitureMap[room.id] = mutableListOf()
        firebase.collection("furniture").whereEqualTo("roomId", room.id).get()
                .addOnSuccessListener {
                    val documents = it.documents
                    for (doc in documents) {
                        val furniture = FurniturFactory(doc)
                        if (furniture != null) {
                            roomToFurnitureMap[room.id]!!.add(furniture.id)
                            furnitureMap[room.id] = furniture
                        }
                    }
                    // change to success only if both room and it's furniture are loaded
                    if (userLoadingStage.value == LoadingStage.LOADING) {
                        areFurnitureLoaded = true
                        if (isRoomLoaded) {
                            loadRoomNavLambda()
                            userLoadingStage.value = LoadingStage.SUCCESS
                            areFurnitureLoaded = false
                            isRoomLoaded = false
                        }
                    }
                }
                .addOnFailureListener {
                    // TODO: is this right?
                    userLoadingStage.value = LoadingStage.FAILURE
                }
    }

    private fun FurniturFactory(doc: DocumentSnapshot) : Furniture? {
        return when(doc["type"]) {
            "Bed" -> doc.toObject(Bed::class.java)
            "Chair" -> doc.toObject(Chair::class.java)
            "Desk" -> doc.toObject(Desk::class.java)
            "Closet" -> doc.toObject(Closet::class.java)
            else  -> null
        }
    }

    fun saveOnExit() {
        for ((roomName, room) in roomsMap) {
            firebase.collection("rooms").document(room.id).set(room)
        }
        for ((furnitureId, furniture) in furnitureMap) {
            firebase.collection("furniture").document(furnitureId).set(furniture)
        }
    }


// TODO: do we need this?

//    fun setRoomNewName(newName: String, oldName: String) {
//        userLoadingStage.value = LoadingStage.LOADING
//        firebase.collection("rooms").whereEqualTo("userID", user.id).whereEqualTo("name", oldName).get()
//                .addOnSuccessListener {
//                    val documents = it.documents
//                    for (doc in documents) {
//                        val room: Room ?= doc.toObject(Room::class.java)
//
//                              ...
//
//                        }
//                    }
//                    userLoadingStage.value = LoadingStage.SUCCESS
//                }
//                .addOnFailureListener {
//                    userLoadingStage.value = LoadingStage.FAILURE
//                }
//    }

}