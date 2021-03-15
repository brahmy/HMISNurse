package com.oss.digihealth.nur.ui.dashboard.view

import com.oss.digihealth.nur.ui.dashboard.model.CommonCount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import kotlinx.android.synthetic.main.row_patients_count.view.*

class PatientsAdapter(internal var context: Context,var data: ArrayList<CommonCount>) : RecyclerView.Adapter<PatientsAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_patients_count, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PatientsAdapter.ViewHolder, position: Int) {
        if(position==1){
            holder.itemView.tv_female_count.visibility = View.VISIBLE
        }else{
            holder.itemView.tv_female_count.visibility = View.VISIBLE
        }
        holder.itemView.tv_title.text = data[position].title
        holder.itemView.tv_male_count.text = data[position].M_Count
        holder.itemView.tv_female_count.text = data[position].F_Count
        holder.itemView.tv_tg_count.text = data[position].T_Count
        holder.itemView.tv_count.text = data[position].Tot_Count
        holder.itemView.cardView.setCardBackgroundColor(data[position].bgColor)
    }


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun updateAdapter(data: ArrayList<CommonCount>) {
        this.data = data
        notifyDataSetChanged()
    }
}

