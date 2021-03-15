package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui.template

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav

class NurseVitualManageTemplateAdapter(context: Context, private var arrayListVitualFavList: ArrayList<ResponseContentsfav?>) : RecyclerView.Adapter<NurseVitualManageTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView
        var serialNumber: TextView
        var deleteImage : ImageView
        var mainLinearLayout: LinearLayoutCompat

        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as LinearLayoutCompat


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(R.layout.row_manage_template_vital, parent, false) as LinearLayoutCompat
        return MyViewHolder(itemLayout)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListVitualFavList[position].toString()
        val list = arrayListVitualFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        holder.textName.text = list!!.vital_master_name
        holder.deleteImage.setOnClickListener({

            onDeleteClickListener?.onDeleteClick(list,position)

        })
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


    }
    override fun getItemCount(): Int {
        return arrayListVitualFavList.size
    }
    fun setFavAddItem(responseContantSave: ArrayList<ResponseContentsfav?>) {
        arrayListVitualFavList = responseContantSave
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<ResponseContentsfav?> {

        return arrayListVitualFavList

    }


    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseData: ResponseContentsfav?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun cleardata() {
        arrayListVitualFavList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        arrayListVitualFavList.removeAt(position)
        notifyDataSetChanged()
    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


