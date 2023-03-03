package me.proton.jobforandroid.material_gb_nasaapi.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.databinding.SettingsFragmentBinding
import me.proton.jobforandroid.material_gb_nasaapi.ui.App


class SettingsFragment : Fragment(), CoroutineScope by MainScope() {

    private lateinit var binding: SettingsFragmentBinding
    private var sharedPreferences: SharedPreferences? = null
    private var themeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        sharedPreferences = activity?.getSharedPreferences(App.APP_PREF_NAME, Context.MODE_PRIVATE)
        themeId = sharedPreferences?.getInt(App.PREF_THEME_KEY, R.style.AppThemeLight)
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        themeSelect.check(
            when (themeId) {
                R.style.AppThemeSand -> R.id.chipSandTheme
                R.style.AppThemeAqua -> R.id.chipAquaTheme
                else -> R.id.chipLightTheme
            }
        )
        chipLightTheme.setOnClickListener {
            changeTheme(1)
        }
        chipSandTheme.setOnClickListener {
            changeTheme(2)
        }
        chipAquaTheme.setOnClickListener {
            changeTheme(3)
        }
    }

    private fun changeTheme(chekChipPosition: Int) {
        sharedPreferences?.edit()?.putInt(App.PREF_THEME_KEY,
            when (chekChipPosition) {
                2 -> R.style.AppThemeSand
                3 -> R.style.AppThemeAqua
                else -> R.style.AppThemeLight
            }
        )?.apply()
        activity?.recreate()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}