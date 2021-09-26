package postpc.finalproject.RoomInn

import postpc.finalproject.RoomInn.furnitureData.*
import postpc.finalproject.RoomInn.models.RoomInnApplication
import java.util.*

class RoomMemoryStack(
    val room: Room
) {
    private val db = RoomInnApplication.getInstance().getRoomsDB()
    private val furnitureStack: Stack<MutableList<Furniture>> = Stack()
    private val poppedStack: Stack<MutableList<Furniture>> = Stack()
    private var isInit: Boolean = false
    private val maxStepAmount: Int = 10

    init {
        if (room.userId != "user id") {
            isInit = true
        }
    }

    fun saveRoomChange() {
        if (isInit) {
            if (furnitureStack.size>0){
            val lastSaveFurniture = furnitureStack[furnitureStack.size - 1]
            val dbCopyList = dbCopyList()
            if (dbCopyList.size == lastSaveFurniture.size) {
                for (i in 0 until dbCopyList.size) {
                    if ((dbCopyList[i] == lastSaveFurniture[i])) {
                        continue
                    } else {
                        furnitureStack.push(dbCopyList())
                        if (furnitureStack.size > maxStepAmount) {
                            furnitureStack.removeAt(0)
                        }
                        return
                    }
                }
            }}else{
                furnitureStack.push(dbCopyList())
                if (furnitureStack.size > maxStepAmount) {
                    furnitureStack.removeAt(0)
                }
                return
            }
        }
    }


    fun undoStep(): Boolean {
        if (isInit && furnitureStack.size > 0) {
            val lastSaveFurniture = furnitureStack.pop()
            val dbCopyList = dbCopyList()
            if (lastSaveFurniture != null) {
                if (dbCopyList.size == lastSaveFurniture.size) {
                    for (i in 0 until dbCopyList.size) {
                        if ((dbCopyList[i] == lastSaveFurniture[i])) {
                            continue
                        } else {
                            poppedStack.push(dbCopyList)
                            if (poppedStack.size > maxStepAmount) {
                                furnitureStack.removeAt(0)
                            }
                            updateRoomFurniture(lastSaveFurniture)
                            return true
                        }
                    }
                }
                return undoStep()

            }

        }
        return false
    }

    fun redoStep(): Boolean {
        if (isInit && poppedStack.size > 0) {
            val lastUndoFurniture = poppedStack.pop()
            if (lastUndoFurniture != null) {
                updateRoomFurniture(lastUndoFurniture)
                return true
            }
        }
        return false
    }

    private fun dbCopyList(): MutableList<Furniture> {
        val newFurniture = mutableListOf<Furniture>()
        db.roomFurniture(room.name).forEach {
            val fur = when (it.type) {
                "Bed" -> Bed(it as Bed)
                "Chair" -> Chair(it as Chair)
                "Desk" -> Desk(it as Desk)
                "Closet" -> Closet(it as Closet)
                "Couch" -> Couch(it as Couch)
                "Dresser" -> Dresser(it as Dresser)
                else -> null
            }
            if (fur != null) {
                newFurniture += fur
            }
        }
        return newFurniture
    }

    private fun updateRoomFurniture(newFurniture: MutableList<Furniture>) {
        val oldFurniture = mutableListOf<Furniture>()
        oldFurniture.addAll(db.roomFurniture(room.name))
        val oldFurnitureIDs = mutableListOf<String>()
        oldFurnitureIDs.addAll(db.roomToFurnitureMap[room.id]!!)
        val newFurnitureIDs = mutableListOf<String>()

        // check if furniture was added, changed
        for (fur in newFurniture) {
            newFurnitureIDs.add(fur.id)
            if (fur.id in oldFurnitureIDs) {
                val furInDB = db.furnitureMap[fur.id]!!
                if (fur != furInDB) { // if furniture was changed
                    db.furnitureMap[fur.id] = fur
                    db.saveFurniture(fur)
                }
            } else { // if furniture was added
                db.furnitureMap[fur.id] = fur
                db.saveFurniture(fur)
            }
        }

        //check if furniture was deleted
        if (newFurniture.size != oldFurnitureIDs.size) {
            for (oldFurID in oldFurnitureIDs) {
                if (oldFurID !in newFurnitureIDs) {
                    db.deleteFurniture(db.furnitureMap[oldFurID]!!)
                }
            }
        }

        // update the furniture map to match the changes
        db.roomToFurnitureMap[room.id] = newFurnitureIDs
    }
}