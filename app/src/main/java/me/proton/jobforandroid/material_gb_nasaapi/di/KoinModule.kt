package me.proton.jobforandroid.material_gb_nasaapi.di

import me.proton.jobforandroid.material_gb_nasaapi.model.repository.Repository
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.RepositoryImpl
import me.proton.jobforandroid.material_gb_nasaapi.ui.main.MainViewModel
import me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment.WorkListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Repository> { RepositoryImpl() }

    viewModel { MainViewModel(get()) }
    viewModel { WorkListViewModel(get()) }
}