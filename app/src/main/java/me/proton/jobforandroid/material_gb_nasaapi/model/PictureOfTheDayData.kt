package me.proton.jobforandroid.material_gb_nasaapi.model

import me.proton.jobforandroid.material_gb_nasaapi.model.repository.PODServerResponseData


sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: PODServerResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}