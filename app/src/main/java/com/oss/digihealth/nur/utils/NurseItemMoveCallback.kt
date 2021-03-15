package com.oss.digihealth.nur.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.view.NurseConfigFavRecyclerAdapter


class NurseItemMoveCallback(private val mAdapter: NurseConfigFavRecyclerAdapter?) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        mAdapter!!.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
                                   actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is NurseConfigFavRecyclerAdapter.MyViewHolder) {
                val myViewHolder = viewHolder as NurseConfigFavRecyclerAdapter.MyViewHolder?
                mAdapter!!.onRowSelected(myViewHolder!!)
            }

        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is NurseConfigFavRecyclerAdapter.MyViewHolder) {
            mAdapter!!.onRowClear(viewHolder)
        }
    }

    interface ItemTouchHelperContract {

        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: NurseConfigFavRecyclerAdapter.MyViewHolder)
        fun onRowClear(myViewHolder: NurseConfigFavRecyclerAdapter.MyViewHolder)

    }




}
