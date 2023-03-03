package me.proton.jobforandroid.material_gb_nasaapi.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.databinding.MainFragmentBinding
import me.proton.jobforandroid.material_gb_nasaapi.model.PictureOfTheDayData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.RepositoryImpl
import me.proton.jobforandroid.material_gb_nasaapi.ui.MainActivity
import me.proton.jobforandroid.material_gb_nasaapi.ui.nav_fragment.BottomNavigationDrawerFragment
import me.proton.jobforandroid.material_gb_nasaapi.ui.settings.SettingsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment.WorkListFragment


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel {
        parametersOf(RepositoryImpl())
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel.getData()
//            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(bottomSheetContainer.root)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetContainer.bottomSheetContainer.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }
        })
        inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${inputEditText.text.toString()}")
            })
        }
        setBottomAppBar(view)
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        chipToday.setOnClickListener {
            viewModel.getData(0)
            chipTransform(it)
        }
        chipYesterday.setOnClickListener {
            viewModel.getData(1)
            chipTransform(it)
        }
        chipDayBeforeYesterday.setOnClickListener {
            viewModel.getData(2)
            chipTransform(it)
        }
        chipToday.performClick()
        imageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                root, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            bottomSheetContainer.bottomSheetContainer.visibility = View.GONE
            if (isExpanded) {
                inputLayout.visibility = View.GONE
                chipsLayout.visibility = View.GONE
                main.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                imageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                inputLayout.visibility = View.VISIBLE
                chipsLayout.visibility = View.VISIBLE
                main.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                imageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            }

        }

    }

    private fun chipTransform(view: View) = with(binding) {
        TransitionManager.beginDelayedTransition(root)
        chipToday.visibility = View.VISIBLE
        chipYesterday.visibility = View.VISIBLE
        chipDayBeforeYesterday.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, WorkListFragment.newInstance())
                    ?.addToBackStack("")
                    ?.commit()
            }
            R.id.app_bar_settings -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SettingsFragment.newInstance())
                    ?.addToBackStack("")
                    ?.commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
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
                        lifecycle(this@MainFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottomSheetContainer.bottomSheetDescription.text =
                        data.serverResponseData.explanation
                    bottomSheetContainer.bottomSheetDescriptionHeader.text =
                        data.serverResponseData.title
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    bottomSheetContainer.bottomSheetContainer.visibility = View.VISIBLE
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

    private fun setBottomAppBar(view: View) = with(binding) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottomAppBar.navigationIcon = null
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
    }
}