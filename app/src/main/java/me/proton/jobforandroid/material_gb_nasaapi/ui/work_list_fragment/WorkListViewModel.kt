package me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.DBResponseData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.Repository
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.RepositoryImpl


class WorkListViewModel (
    private val repository: Repository = RepositoryImpl(),
): ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {
    val liveData: MutableLiveData<List<DBResponseData>> = MutableLiveData()

    fun getData(): LiveData<List<DBResponseData>> {
        liveData.postValue(repository.getAllWorkListFromDB())
        return liveData
    }

    fun addNote(dbResponseData: DBResponseData) {
        launch(Dispatchers.IO) {
            repository.saveWorkNoteToDB(dbResponseData)
        }
    }
}