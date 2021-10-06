package postpc.finalproject.RoomInn.models

data class User(
        var id: String = "not initialized id",
        var roomsList: MutableList<String> = mutableListOf(), // string of the rooms names
        var firstRun: Boolean = true,
        var firstPlay: Boolean = true
) {
}