package com.oss.digihealth.nur.ui.dashboard.view

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.ZeroStockResponseContent
import kotlinx.android.synthetic.main.row_patients_complients.view.*

class ZeroStockAdapter(var context: Context, var data: List<ZeroStockResponseContent>) : RecyclerView.Adapter<ZeroStockAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZeroStockAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_patients_complients, parent, false)
        return ViewHolder(v)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.color.white)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.color.alternateRow)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ZeroStockAdapter.ViewHolder, position: Int) {
        holder.itemView.ll_parent.weightSum = 10f
        holder.itemView.tv_name.setLayoutParams(
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,10f
            )
        )

        holder.itemView.tv_count.setLayoutParams(
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,10f
            )
        )


        if(position % 2 == 1){
            setContentBg(holder.itemView.tv_name)
            setContentBg(holder.itemView.tv_count)
        }else{
            setHeaderBg(holder.itemView.tv_name)
            setHeaderBg(holder.itemView.tv_count)
        }


        val rowPos = holder.adapterPosition
        if(rowPos == 0){
            holder.itemView.apply {
                tv_name.text = "Name"
                tv_count .visibility = View.GONE
                tv_code.visibility = View.GONE
                tv_name.setTextColor(context.resources.getColor(R.color.black))
                tv_name.setTypeface(null, Typeface.BOLD)
            }
        }
        else {
            val dataModel = data?.get(rowPos - 1)
            holder.itemView.tv_code.visibility = View.GONE
            holder.itemView.tv_count.visibility = View.GONE
            holder.itemView.tv_name.setTextColor(context.resources.getColor(R.color.navColor))
            //holder.itemView.tv_code.text = data?.get(position)?.d_code.toString()
            holder.itemView.tv_name.text = dataModel.item_master.name
            holder.itemView.tv_name.gravity = Gravity.LEFT
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data!!.size + 1
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
