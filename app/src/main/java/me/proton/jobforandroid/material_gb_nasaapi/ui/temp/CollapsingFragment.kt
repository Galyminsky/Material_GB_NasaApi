package ru.s1aks.picoftheday.ui.temp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.proton.jobforandroid.material_gb_nasaapi.databinding.CollapsingFragmentBinding


class CollapsingFragment : Fragment() {
    private lateinit var binding: CollapsingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = CollapsingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = CollapsingFragment()
    }
}