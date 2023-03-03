package me.proton.jobforandroid.material_gb_nasaapi.di

import me.proton.jobforandroid.material_gb_nasaapi.model.repository.PODRetrofitImpl
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.Repository
import me.proton.jobforandroid.material_gb_nasaapi.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Repository> { PODRetrofitImpl() }

    viewModel { MainViewModel(get()) }
}