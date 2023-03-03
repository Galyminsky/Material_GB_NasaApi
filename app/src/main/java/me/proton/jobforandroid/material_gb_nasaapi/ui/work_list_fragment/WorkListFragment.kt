package me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment

import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.proton.jobforandroid.material_gb_nasaapi.databinding.WorklistFragmentBinding
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.DBResponseData
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.RepositoryImpl
import me.proton.jobforandroid.material_gb_nasaapi.ui.App
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment.WorkListAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class WorkListFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: WorklistFragmentBinding
    private val viewModel: WorkListViewModel by viewModel {
        parametersOf(RepositoryImpl())
    }
    private var adapter: WorkListAdapter? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = WorklistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter = WorkListAdapter(
                object : OnListItemClickListener {
                    override fun onItemClick(data: DBResponseData) {
                    }
                },
                it.toMutableList(),
                object : OnStartDragListener {
                    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                        itemTouchHelper.startDrag(viewHolder)
                    }
                })

            recycler.adapter = adapter
            fab.setOnClickListener {
                val newData = DBResponseData(
                    0,
                    adapter!!.itemCount,
                    1,
                    "Title of note",
                    "Content of note",
                    LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(App.DATE_TIME_FORMAT_PATTERN))
                )
                viewModel.addNote(newData)
                adapter?.appendItem(newData)
            }
            itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
            itemTouchHelper.attachToRecyclerView(recycler)
        }
        launch(Dispatchers.IO)
        {
            viewModel.getData()
        }
    }

//    private fun changeAdapterData() {
//        adapter.setItems(createItemList(isNewList).map { it })
//        isNewList = !isNewList
//    }

    interface OnListItemClickListener {
        fun onItemClick(data: DBResponseData)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    companion object {
        fun newInstance() = WorkListFragment()
    }
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}

class ItemTouchHelperCallback(private val adapter: WorkListAdapter?) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        adapter?.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        adapter?.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
        itemViewHolder.onItemClear()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val width = viewHolder.itemView.width.toFloat()
            val alpha = 1.0f - abs(dX) / width
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(
                c, recyclerView, viewHolder, dX, dY,
                actionState, isCurrentlyActive
            )
        }
    }
}
