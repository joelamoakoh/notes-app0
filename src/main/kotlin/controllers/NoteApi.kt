package controllers

import models.Note
import persistence.Serializer

class NoteAPI (serializerType: Serializer) {
    private var notes = ArrayList<Note>()

    private var serializer: Serializer = serializerType

    fun add(note: Note): Boolean {
        return notes.add(note)
    }
    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else notes.joinToString (separator = "\n") { note ->
            notes.indexOf(note).toString() + ": " + note.toString() }


    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0) "NO ACTIVE NOTES STORED"
        else formatListString(notes.filter { note -> !note.isNoteArchived })


    private fun formatListString(notesToFormat: List<Note>): String =   //use of function to stop repitition
        notesToFormat
            .joinToString(separator = "\n") { note ->
                notes.indexOf(note).toString() + " " + note.toString()
            }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() == 0) {
            "NO ARCHIVED NOTES STORED"
        } else {
            var listOfArchivedNotes = ""
            for (note in notes) {
                if (note.isNoteArchived) {
                    listOfArchivedNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfArchivedNotes
        }
    }

    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }


    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }
    fun numberOfNotesByPriority(priority: Int): Int = notes.count { note: Note -> note.notePriority == priority }

    
    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfNotes +=
                        """$i: ${notes[i]}
                        """.trimIndent()
                }
            }
            if (listOfNotes == "") {
                "No notes with priority: $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }

        }
    }
    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }
    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        val foundNote = findNote(indexToUpdate)

        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

}
