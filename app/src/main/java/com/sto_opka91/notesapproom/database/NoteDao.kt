package com.sto_opka91.notesapproom.database


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sto_opka91.notesapproom.models.ModelNote

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: ModelNote)
    @Delete
    suspend fun delete (note: ModelNote)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<ModelNote>>

    @Query("UPDATE notes_table SET title = :title, note = :note WHERE id = :id ")
    suspend fun updateNote(id: Int?, title: String?, note: String?)
}