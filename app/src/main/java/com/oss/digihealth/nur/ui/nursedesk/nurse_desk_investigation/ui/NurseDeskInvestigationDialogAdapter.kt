package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResultResponseContent

class NurseDeskInvestigationDialogAdapter(private val context: Context) :
    RecyclerView.Adapter<NurseDeskInvestigationDialogAdapter.MyViewHolder>() {

    private var bytestream: Bitmap? = null
    private var imageposition : Int?=0
    private var manageLabPrevLabArrayList: List<NurseDeskInvestigationResultResponseContent?>? = ArrayList()
    private  var onListItemClickListener: OnListItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_investigation_nurse_desk_dialog, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return manageLabPrevLabArrayList!!.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val responseData = manageLabPrevLabArrayList?.get(position);
        holder.dateTextView.text =
            responseData?.created_date + "-" + responseData?.test_master?.name
        holder.DescriptionTextView.text = responseData?.result_value
        holder.previewLinearLayout.setOnClickListener {
            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE
            } else {
                onListItemClickListener!!.onListItemClick(responseData?.image_url?.get(position)?.file_path,position)
                holder.resultLinearLayout.visibility = View.VISIBLE
            }
        }
        if(bytestream!=null)
        {
            holder.img_view.setImageBitmap(bytestream)
        }
    }

    fun setData(responseContents: List<NurseDeskInvestigationResultResponseContent?>?) {
        manageLabPrevLabArrayList = responseContents!!
        notifyDataSetChanged()
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        internal val DescriptionTextView : TextView = itemView.findViewById(R.id.Description)
        internal val img_view : ImageView = itemView.findViewById(R.id.resultimage)
        internal val previewLinearLayout: LinearLayout = itemView.findViewById(R.id.previewLinearLayout)
        internal val resultLinearLayout: CardView = itemView.findViewById(R.id.resultLinearLayout)

    }

    interface OnListItemClickListener {
        fun onListItemClick(responseContent: String?, position: Int) {

        }
    }

    fun setOnListItemClickListener(onListItemClickListener: OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }

    fun setImage(byteStream: Bitmap, imgposition: Int) {

        bytestream = byteStream
        imageposition = imgposition
        notifyDataSetChanged()

    }

}