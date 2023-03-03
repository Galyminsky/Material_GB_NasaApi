package me.proton.jobforandroid.material_gb_nasaapi.ui.pod_fragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.proton.jobforandroid.material_gb_nasaapi.BuildConfig
import me.proton.jobforandroid.material_gb_nasaapi.model.PictureOfTheDayData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.PODRetrofitImpl
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.PODServerResponseData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PODViewModel(
    private val retrofitImpl: Repository = PODRetrofitImpl(),
) : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {
    val liveData: MutableLiveData<PictureOfTheDayData> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(daysBefore: Long): LiveData<PictureOfTheDayData> {
        val currentDateTime = LocalDateTime.now()
        launch {
            sendServerRequest(currentDateTime.minusDays(daysBefore))
        }
        return liveData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendServerRequest(date: LocalDateTime) {
        liveData.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl()
                .getPictureOfTheDay(apiKey, date.format(DateTimeFormatter.ISO_DATE))
                .enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>,
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveData.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveData.value =
                                    PictureOfTheDayData.Error(Throwable("Unidentified error"))
                            } else {
                                liveData.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveData.value = PictureOfTheDayData.Error(t)
                    }
                })
        }
    }
}