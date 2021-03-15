package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response.NurseDeskVitalsresponseContent

typealias OnClickAction = (NurseDeskVitalsresponseContent) -> Unit

class NurseDeskVitalsAdapter(
    private val context: Context,

    private var vitalsArrayList: ArrayList<NurseDeskVitalsresponseContent?>,
    val onClickAction: OnClickAction
) :
    RecyclerView.Adapter<NurseDeskVitalsAdapter.MyViewHolder>() {
    private var isLoadingAdded = false
    private var onNextclickListener: nextClickListerner? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_nursedesk_vitals, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vitalsArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = vitalsArrayList[position]
        holder.vitalsSerialno.text = (position + 1).toString()
        holder.vitalsPin.text = responseContent?.patient_uhid
        holder.vitalMobileNumber.text = responseContent?.patient_mobile



        if (responseContent?.ward_transfer_status?.code == "REQUEST") {

            holder.vitalsAction.setImageResource(R.drawable.ic_vitals_action_grey)
        } else {


            holder.vitalsAction.setImageResource(R.drawable.ic_vitals_action_active)

        }


        if (responseContent?.patient_info != null) {
            holder.vitalsPatientInformation.text = responseContent.patient_info
        } else {

            if (responseContent?.patient_title_name.equals(null)) {
                responseContent?.patient_title_name = ""
            }
            holder.vitalsPatientInformation.text =
                responseContent?.patient_title_name + "" + responseContent?.patient_first_name + "/" +
                        responseContent?.patient_gender_name + "/" + responseContent?.patient_age
        }




        holder.vitalsAction.setOnClickListener {

            if (responseContent?.ward_transfer_status?.code != "REQUEST") {

                onClickAction.invoke(responseContent!!)
            }

            //onNextclickListener?.nextClick()

        }

        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }

    }

    interface nextClickListerner {
        fun nextClick()
    }

    fun setnextClickListerner(onnextClickListerner: nextClickListerner) {
        this.onNextclickListener = onnextClickListerner
    }


    fun addAll(responseContent: List<NurseDeskVitalsresponseContent?>?) {
        this.vitalsArrayList.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        vitalsArrayList.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val vitalsSerialno: TextView = itemView.findViewById(R.id.vitalsSerialno)
        internal val vitalsPin: TextView = itemView.findViewById(R.id.vitalsPin)
        internal val vitalMobileNumber: TextView = itemView.findViewById(R.id.vitalsMobileNumber)
        internal val vitalsPatientInformation: TextView =
            itemView.findViewById(R.id.vitalsPatientInformation)
        internal val vitalsAction: ImageView = itemView.findViewById(R.id.vitalsAction)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
    }

}