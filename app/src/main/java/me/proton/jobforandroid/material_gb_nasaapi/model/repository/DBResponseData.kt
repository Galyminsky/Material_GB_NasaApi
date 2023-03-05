package me.proton.jobforandroid.material_gb_nasaapi.model.repository

import com.google.gson.annotations.SerializedName

data class DBResponseData(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("position") val position: Int,
    @field:SerializedName("priority") val priority: Int,
    @field:SerializedName("note_title") val note_title: String,
    @field:SerializedName("note_content") val note_content: String,
    @field:SerializedName("time") val time: String
)
