package me.proton.jobforandroid.material_gb_nasaapi.model.repository



interface Repository {

    fun getRetrofitImpl(): PictureOfTheDayAPI
    fun saveWorkNoteToDB(dbResponseData: DBResponseData)
    fun getAllWorkListFromDB(): List<DBResponseData>
}