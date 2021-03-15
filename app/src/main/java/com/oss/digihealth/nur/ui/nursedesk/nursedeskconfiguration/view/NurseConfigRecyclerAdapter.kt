package com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.view

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseContent

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NurseConfigRecyclerAdapter(private val mContext: Context?, private var configResponseContent: ArrayList<ConfigResponseContent?>) :
    RecyclerView.Adapter<NurseConfigRecyclerAdapter.MyViewHolder>() {
    private var configlistdata: ArrayList<ConfigResponseContent?> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.config_list, parent, false)
        return MyViewHolder(v)
    }
    override fun getItemCount(): Int {
        return configResponseContent.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var config_Name_TextView: TextView
        internal var img_normallist: ImageView
        init {
            config_Name_TextView  = itemView.findViewById(R.id.config_name)
            img_normallist  = itemView.findViewById(R.id.img_normallist)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val configlistdata = configResponseContent[position]
        holder.config_Name_TextView.text = configlistdata?.activity?.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(configlistdata!!, position)
        }
        if(configlistdata?.activity?.code.equals("01")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_vitals_icon)
        }
        //Lab
        else if(configlistdata?.activity?.code.equals("Lab02")!!)
        {

            holder.img_normallist.setImageResource(R.drawable.ic_widget_lab)
        }//Radiology
        else if(configlistdata?.activity?.code.equals("rad02")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_radiology)
        }
        //Prescrip
        else if(configlistdata?.activity?.code.equals("drug01")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_prescription)
        }//inves
        else if(configlistdata?.activity?.code.equals("Inve03")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_investigation)
        }//bedmanage
        if(configlistdata?.activity?.code.equals("BEDMAN001")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_bed_management)
        }//notes
        else if(configlistdata?.activity?.code.equals("No04")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_notes)
        }
        //diet
        else if(configlistdata?.activity?.code.equals("Diet1")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_diet_order)
        }
        //discha
        else  if(configlistdata?.activity?.code.equals("DIS001")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_discharge_summary)
        }
        //creticak
        else  if(configlistdata?.activity?.code.equals("Crit06")!!)
        {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_critical_case_sheet)
        }

    }
    fun setConfigList(configResponse: ConfigResponseContent?) {

        val check= configResponseContent.any{ it!!.activity_uuid == configResponse?.activity_uuid}
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(configResponseContent: ConfigResponseContent, position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
    fun removeitem(position: Int) {
        this.configResponseContent.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }



    fun setConfigItem(responseContents: ArrayList<ConfigResponseContent?>) {
        this.configResponseContent = responseContents
        this.configlistdata = responseContents
        notifyDataSetChanged()

    }


    fun getConfigData(): ArrayList<ConfigResponseContent?> {
        return this.configResponseContent
    }

    fun removeall(conficFinalData: ArrayList<ConfigResponseContent?>?) {
        for(i in conficFinalData!!.indices)
        {
            this.configResponseContent = conficFinalData
            this.configResponseContent.removeAt(0)
            notifyItemRemoved(0);
            notifyItemRangeChanged(0, getItemCount() - 0);

        }
    }

    fun getItemSize(): Int {
        return this.configResponseContent.size
    }

    fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    configResponseContent = configlistdata
                } else {

                    val filteredList = java.util.ArrayList<ConfigResponseContent?>()
                    for (messageList in configlistdata!!) {
                        if (messageList?.activity?.name != null) {
                            if (messageList?.activity?.name?.toLowerCase()?.contains(
                                    charString
                                )!!
                            ) {

                                filteredList.add(messageList)
                            }
                        }
                    }
                    configResponseContent = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = configResponseContent
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                configResponseContent = filterResults.values as java.util.ArrayList<ConfigResponseContent?>
                notifyDataSetChanged()
            }
        }
    }

    fun setConfigfavList(configResponse: ConfigResponseContent?) {

        val check= configResponseContent.any{ it!!.activity_uuid == configResponse?.activity_uuid}
        if (!check) {
            this.configResponseContent.add(configResponse)
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }

    }

/*    fun setConfigItemRemove(responseContents: ResponseContent?) {

        Log.i("",""+responseContents)

        val check= configResponseContent.any{ it!!.activity_uuid == responseContents?.activity_id}
        if (check) {
            this.configResponseContent.remo
            notifyDataSetChanged()
        }
        else{
            notifyDataSetChanged()
        }

    }*/

    fun clearALL() {
        this?.configResponseContent?.clear()
        notifyDataSetChanged()
    }

}



