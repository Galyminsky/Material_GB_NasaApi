package me.proton.jobforandroid.material_gb_nasaapi.ui.work_list_fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.proton.jobforandroid.material_gb_nasaapi.databinding.WorklistRecyclerItemBinding
import me.proton.jobforandroid.material_gb_nasaapi.model.repository.DBResponseData


class WorkListAdapter(
    private val onListItemClickListener: WorkListFragment.OnListItemClickListener,
    private var data: MutableList<DBResponseData>,
    private val dragListener: WorkListFragment.OnStartDragListener,
) :
    RecyclerView.Adapter<WorkListAdapter.WorkListViewHolder>(), ItemTouchHelperAdapter {

    fun setData(data: List<DBResponseData>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }

    fun appendItem(newData: DBResponseData) {
        data.add(newData)
        notifyItemInserted(data.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkListViewHolder(
        WorklistRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: WorkListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItems(newItems: List<DBResponseData>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    inner class DiffUtilCallback(
        private var oldItems: List<DBResponseData>,
        private var newItems: List<DBResponseData>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].note_content == newItems[newItemPosition].note_content

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                oldItem,
                newItem
            )
        }
    }

    inner class WorkListViewHolder(private val binding: WorklistRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(data: DBResponseData) = with(binding) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                title.text = data.note_title
                content.text = data.note_content
                time.text = data.time
                root.setOnClickListener { onListItemClickListener.onItemClick(data) }
                moveUp.setOnClickListener { moveUp() }
                moveDown.setOnClickListener { moveDown() }
                removeItemImageView.setOnClickListener { removeItem() }
                dragHandleImageView.setOnTouchListener { _, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this@WorkListViewHolder)
                    }
                    false
                }
            }

        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }
}

