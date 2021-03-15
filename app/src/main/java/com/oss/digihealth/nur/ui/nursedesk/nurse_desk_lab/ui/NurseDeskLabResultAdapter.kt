package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.ResponseContentX

class NurseDeskLabResultAdapter(private val context: Context) :
    RecyclerView.Adapter<NurseDeskLabResultAdapter.MyViewHolder>() {
    private var manageLabPrevLabArrayList: List<ResponseContentX?>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_nurse_desk_lab_result_dialog, parent, false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return manageLabPrevLabArrayList!!.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val responseData = manageLabPrevLabArrayList?.get(position);
        holder.sNoTextView.setText((position + 1).toString())
        holder.observationTxtView.setText(responseData?.test_or_analyte)
        holder.resultTextView.setText(responseData?.result_value)
        holder.uomTextView.setText("")
        holder.referenceRangeTextView.setText("")
    }
    fun setData(responseContents: List<ResponseContentX?>?) {
        manageLabPrevLabArrayList = responseContents!!
        notifyDataSetChanged()
    }
    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val sNoTextView: TextView = itemView.findViewById(R.id.sNoTextView)
        internal val observationTxtView: TextView = itemView.findViewById(R.id.observationTxtView)
        internal val resultTextView: TextView = itemView.findViewById(R.id.resultTextView)
        internal val uomTextView: TextView = itemView.findViewById(R.id.uomTextView)
        internal val referenceRangeTextView: TextView = itemView.findViewById(R.id.referenceRangeTextView)
    }

}