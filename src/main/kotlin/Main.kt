import controllers.NoteAPI
import models.Note
import utils.ScannerInput
import java.lang.System.exit
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File

private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))


private val logger = KotlinLogging.logger {}



fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note
         > > ----------------------------------
         > |   20) Save a note               |
         > |   21) Load a note               |
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archiveNote()
            20  -> save()
            21 -> load()
            0  -> exitApp()
            else -> println("Invalid option entered:  ${option}")
        }
    } while (true)
}
fun addNote(){
    logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}


fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNote();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}



fun updateNote() {
    logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}


fun deleteNote(){
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Note Deleted !!! Deleted note: ${noteToDelete.noteTitle}")
    } else {
        println("Delete NOT Successful")
    }
    }
}



fun exitApp(){
    println("Exiting...bye")
    exit(0)
}
fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
fun listActiveNotes(){
    println(noteAPI.listActiveNotes())
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}
fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listArchivedNote() {
    println(noteAPI.listArchivedNotes())
}


