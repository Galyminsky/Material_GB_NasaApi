package me.proton.jobforandroid.material_gb_nasaapi.ui.earth_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import me.proton.jobforandroid.material_gb_nasaapi.BuildConfig
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.databinding.PageEarthFragmentBinding
import me.proton.jobforandroid.material_gb_nasaapi.model.PictureOfTheDayData

class EarthFragment: Fragment() {
    private lateinit var binding: PageEarthFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = PageEarthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            val url = PIC_URL + apiKey
                imageView.load(url) {
                    lifecycle(this@EarthFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
            title.text = TITLE
        }
    }

    companion object {
        fun newInstance() = EarthFragment()
        private const val TITLE = "MOSCOW"
        private const val PIC_URL = "https://api.nasa.gov/planetary/earth/imagery?lon=37.618423&lat=55.751244&dim=0.25&"
    }
}