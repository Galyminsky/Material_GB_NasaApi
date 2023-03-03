package me.proton.jobforandroid.material_gb_nasaapi.model.repository

import com.google.gson.GsonBuilder
import me.proton.jobforandroid.material_gb_nasaapi.model.database.Database
import me.proton.jobforandroid.material_gb_nasaapi.model.database.WorkNoteEntity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryImpl : Repository {

    private val baseUrl = "https://api.nasa.gov/"

    override fun getRetrofitImpl(): PictureOfTheDayAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return podRetrofit.create(PictureOfTheDayAPI::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    override fun saveWorkNoteToDB(dbResponseData: DBResponseData) {
        Database.db.workNotesDao().insert(convertDataToEntity(dbResponseData))
    }

    override fun getAllWorkListFromDB(): List<DBResponseData> {
        return convertEntityToData(Database.db.workNotesDao().all())
    }

    private fun convertEntityToData(entityList: List<WorkNoteEntity>): List<DBResponseData> =
        entityList.map {
            DBResponseData(it.id, it.position, it.priority, it.note_title, it.note_content, it.time)
        }

    private fun convertDataToEntity(dbResponseData: DBResponseData): WorkNoteEntity =
        WorkNoteEntity(0,
            dbResponseData.position,
            dbResponseData.priority,
            dbResponseData.note_title,
            dbResponseData.note_content,
            dbResponseData.time
        )
}
