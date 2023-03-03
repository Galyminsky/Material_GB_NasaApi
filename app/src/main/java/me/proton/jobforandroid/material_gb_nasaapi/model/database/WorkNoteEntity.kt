package me.proton.jobforandroid.material_gb_nasaapi.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val position: Int,
    val priority: Int,
    val note_title: String,
    val note_content: String,
    val time: String
)
