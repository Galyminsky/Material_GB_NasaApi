package me.proton.jobforandroid.material_gb_nasaapi.model

import me.proton.jobforandroid.material_gb_nasaapi.model.repository.DBResponseData


sealed class WorkNoteData {
    data class Success(val dbResponseData: DBResponseData) : WorkNoteData()
    data class Error(val error: Throwable) : WorkNoteData()
    data class Loading(val progress: Int?) : WorkNoteData()
}