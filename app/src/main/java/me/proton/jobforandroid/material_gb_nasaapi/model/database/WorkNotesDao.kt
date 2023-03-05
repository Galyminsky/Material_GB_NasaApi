package me.proton.jobforandroid.material_gb_nasaapi.model.database

import androidx.room.*

@Dao
interface WorkNotesDao {
    @Query("SELECT * FROM WorkNoteEntity")
    fun all(): List<WorkNoteEntity>

    @Query("SELECT * FROM WorkNoteEntity WHERE note_title LIKE :word")
    fun getDataByWord(word: String): List<WorkNoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: WorkNoteEntity)

    @Update
    fun update(entity: WorkNoteEntity)

    @Delete
    fun delete(entity: WorkNoteEntity)

    @Query("DELETE FROM WorkNoteEntity WHERE id = :id")
    fun deleteById(id: Int)
}