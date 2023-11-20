package com.sto_opka91.notesapproom.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sto_opka91.notesapproom.models.ModelNote

class NotesRepository(private val noteDao: NoteDao) {
    val allNotes : LiveData<List<ModelNote>> = noteDao.getAllNotes()
    suspend fun insert(note: ModelNote){
        noteDao.insert(note)
    }
    suspend fun delete(note: ModelNote){
        noteDao.delete(note)
    }
    suspend fun update(note: ModelNote){
        noteDao.updateNote(note.id,note.title,note.note)
    }
}