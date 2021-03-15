package com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.view

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseContent
import com.oss.digihealth.nur.utils.NurseItemMoveCallback
import com.oss.digihealth.nur.utils.StartDragListener

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.ui.emr_workflow.history.model.response.History
import com.oss.digihealth.nur.ui.emr_workflow.model.ResponseContent
import java.util.*
import kotlin.collections.ArrayList

class NurseConfigFavRecyclerAdapter(
    private val mStartDragListener: StartDragListener,
    private var configContent: ArrayList<ConfigResponseContent?>
) : RecyclerView.Adapter<NurseConfigFavRecyclerAdapter.MyViewHolder>(),
    NurseItemMoveCallback.ItemTouchHelperContract {

    private var onItemClickListener: OnItemClickListener? = null
    private var boolean: Boolean? = false
    private var topos: Int? = 0
    private var from_pos: Int? = 0

    //private var configFavArrList: List<com.oss.digihealth.doc.ui.emr_workflow.model.EMR_Response.ResponseContent>? = null
    private lateinit var context: Context
    private var arrayListConfigData: ArrayList<ConfigResponseContent?> = ArrayList()


    class MyViewHolder(internal var rowView: View) : RecyclerView.ViewHolder(rowView) {

        val mTitle: TextView

        //        val diaplay_order: TextView
        internal var imageView: ImageView
        internal var img_normallist: ImageView

        init {
            mTitle = rowView.findViewById(R.id.config_name)
//            diaplay_order = rowView.findViewById(R.id.order_no)
            imageView = rowView.findViewById(R.id.item_remove)
            img_normallist = rowView.findViewById(R.id.img_normallist)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.config_fav_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.mTitle.text = configContent[position]!!.activity?.name

//        holder.diaplay_order.text = configContent[position]!!.activity?.display_order.toString()

        holder.imageView.setOnClickListener {
            onItemClickListener?.onItemClick(configContent[position]!!, position)
        }
        holder.itemView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder)
            }
            false
        }
        if (configContent[position]!!.activity?.code.equals("01")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_vitals_icon)
        }
        //Lab
        else if (configContent[position]!!.activity?.code.equals("Lab02")) {

            holder.img_normallist.setImageResource(R.drawable.ic_widget_lab)
        }//Radiology
        else if (configContent[position]!!.activity?.code.equals("rad02")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_radiology)
        }
        //Prescrip
        else if (configContent[position]!!.activity?.code.equals("drug01")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_prescription)
        }//inves
        else if (configContent[position]!!.activity?.code.equals("Inve03")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_investigation)
        }//bedmanage
        if (configContent[position]!!.activity?.code.equals("BEDMAN001")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_bed_management)
        }//notes
        else if (configContent[position]!!.activity?.code.equals("No04")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_notes)
        }
        //diet
        else if (configContent[position]!!.activity?.code.equals("Diet1")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_diet_order)
        }
        //discha
        else if (configContent[position]!!.activity?.code.equals("DIS001")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_discharge_summary)
        }
        //creticak
        else if (configContent[position]!!.activity?.code.equals("Crit06")) {
            //vital
            holder.img_normallist.setImageResource(R.drawable.ic_widget_critical_case_sheet)
        }


    }

    override fun getItemCount(): Int {
        return configContent.size
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        boolean = true
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(configContent, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(configContent, i, i - 1)
            }
        }
        topos = toPosition
        from_pos = fromPosition
        notifyItemMoved(fromPosition, toPosition)


    }

    override fun onRowSelected(myViewHolder: MyViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY)
    }

    override fun onRowClear(myViewHolder: MyViewHolder) {
        try {
            myViewHolder.rowView.setBackgroundColor(Color.WHITE)
            notifyDataSetChanged()
        } catch (e: Exception) {

        }

    }


    fun setConfigfavList(configResponseContent: ConfigResponseContent) {
        boolean = false
        this.configContent.add(configResponseContent)
        notifyDataSetChanged()
    }


    fun setConfigfavarrayList(workflowOrderResponseContent: List<ResponseContent?>) {

        boolean = false
        for (i in workflowOrderResponseContent.indices) {
            val configResponseContent: ConfigResponseContent = ConfigResponseContent()

            configResponseContent.activity?.name =
                workflowOrderResponseContent.get(i)?.activity_name
            configResponseContent.activity?.code =
                workflowOrderResponseContent.get(i)?.activity_code
            configResponseContent.context_uuid =
                workflowOrderResponseContent.get(i)?.context_activity_map_uuid
            configResponseContent.order = workflowOrderResponseContent.get(i)?.order
            configResponseContent.activity_uuid = workflowOrderResponseContent.get(i)?.activity_uuid
            arrayListConfigData.add(configResponseContent)
        }

        this.configContent = arrayListConfigData
        notifyDataSetChanged()
    }

    fun getFinalData(): ArrayList<ConfigResponseContent?> {
        return configContent
    }

    interface OnItemClickListener {
        fun onItemClick(configfavResponseContent: ConfigResponseContent?, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun getItemSize(): Int {

        return this.configContent.size
    }

    fun removeitem(position: Int) {
        this.configContent.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)
        notifyDataSetChanged()  //
    }

    fun getConfigFavData(): ArrayList<ConfigResponseContent?> {
        return this.configContent
    }

    fun clearALL() {
        this.configContent.clear()
        notifyDataSetChanged()
    }

    fun setConfigHistoryarrayList(workflowOrderResponseContent: List<History?>?) {

        boolean = false
        for (i in workflowOrderResponseContent?.indices!!) {
            val configResponseContent: ConfigResponseContent = ConfigResponseContent()
            configResponseContent.activity?.display_order =
                workflowOrderResponseContent.get(i)?.work_flow_order
            configResponseContent.activity?.name =
                workflowOrderResponseContent.get(i)?.activity_name
            configResponseContent.context_uuid =
                workflowOrderResponseContent.get(i)?.emr_history_settings_id
            configResponseContent.activity_uuid = workflowOrderResponseContent.get(i)?.activity_id
            arrayListConfigData.add(configResponseContent)
        }

        this.configContent = arrayListConfigData
        notifyDataSetChanged()
    }
}

