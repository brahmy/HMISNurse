package com.oss.digihealth.nur.ui.dashboard.view

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.dashboard.model.Diagnosis
import kotlinx.android.synthetic.main.row_patients_complients.view.*

class DiagnosisAdapter(var context: Context, var data: ArrayList<Diagnosis>?) :
    RecyclerView.Adapter<DiagnosisAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_patients_complients, parent, false)
        return ViewHolder(v)
    }


    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.color.white)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.color.alternateRow)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: DiagnosisAdapter.ViewHolder, position: Int) {


        if (position % 2 == 1) {
            setContentBg(holder.itemView.tv_name)
            setContentBg(holder.itemView.tv_count)
            setContentBg(holder.itemView.tv_code)
        } else {
            setHeaderBg(holder.itemView.tv_name)
            setHeaderBg(holder.itemView.tv_count)
            setHeaderBg(holder.itemView.tv_code)
        }

        val rowPos = holder.adapterPosition

        if (rowPos == 0) {
            holder.itemView.apply {
                tv_code.text = "CODE"
                tv_name.text = "Name"
                tv_count.text = "COUNT"
                tv_name.setTextColor(context.resources.getColor(R.color.black))
                tv_code.setTextColor(context.resources.getColor(R.color.black))
                tv_count.setTextColor(context.resources.getColor(R.color.black))
                tv_name.setTypeface(null,Typeface.BOLD)
                tv_code.setTypeface(null,Typeface.BOLD)
                tv_count.setTypeface(null,Typeface.BOLD)
            }
        } else {
            val dataModel = data?.get(rowPos - 1)
            holder.itemView.tv_count.text = dataModel?.Count.toString()
            holder.itemView.tv_code.text = dataModel?.pd_diagnosis_uuid.toString()
            holder.itemView.tv_name.text = dataModel?.d_name.toString()
            holder.itemView.tv_name.setTextColor(context.resources.getColor(R.color.navColor))
            holder.itemView.tv_code.setTextColor(context.resources.getColor(R.color.navColor))
            holder.itemView.tv_count.setTextColor(context.resources.getColor(R.color.navColor))
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