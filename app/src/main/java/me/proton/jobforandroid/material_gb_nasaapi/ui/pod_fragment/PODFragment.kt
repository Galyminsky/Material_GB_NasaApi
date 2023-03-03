package me.proton.jobforandroid.material_gb_nasaapi.ui.pod_fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import coil.load
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.databinding.PagePodFragmentBinding
import me.proton.jobforandroid.material_gb_nasaapi.model.PictureOfTheDayData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.PODRetrofitImpl
import me.proton.jobforandroid.material_gb_nasaapi.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PODFragment : Fragment() {
    private lateinit var binding: PagePodFragmentBinding
    private val viewModel: PODViewModel by viewModel {
        parametersOf(PODRetrofitImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = PagePodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData(0)
        chipToday.setOnClickListener {
            viewModel.getData(0)
        }
        chipYesterday.setOnClickListener {
            viewModel.getData(1)
        }
        chipDayBeforeYesterday.setOnClickListener {
            viewModel.getData(2)
        }
    }

    private fun renderData(data: PictureOfTheDayData) = with(binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //showError("Сообщение, что ссылка пустая")
                    toast("Link is empty")
                } else {
                    //showSuccess()
                    imageView.load(url) {
                        lifecycle(this@PODFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    title.text = data.serverResponseData.title
                    overview.text = data.serverResponseData.explanation
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

    companion object {
        fun newInstance() = PODFragment()
    }
}