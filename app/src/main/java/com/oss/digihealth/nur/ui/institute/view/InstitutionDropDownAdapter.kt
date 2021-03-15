package com.oss.digihealth.nur.ui.institute.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.institute.model.InstitutionResponseContent

import kotlin.collections.ArrayList

class InstitutionDropDownAdapter(val context: Context, var listOfficeItems: ArrayList<InstitutionResponseContent?>) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.view_drop_down_institution, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        val params = view.layoutParams
        // params.height = 60
        view.layoutParams = params

        if(listOfficeItems[position]!!.name?.equals("")!!) {
            vh.label.hint = "Please select Institution"
        }
        else{
            vh.label.text = listOfficeItems[position]!!.name
        }

        return view
    }

    override fun getItem(position: Int): Any? {
        return 0

    }
    override fun getItemId(position: Int): Long {

        return 0
    }


    override fun getCount(): Int {
        val count = listOfficeItems.size
        return if (count > 0) count - 1 else count
    }

    fun setInstitutionListDetails(responseContents: ArrayList<InstitutionResponseContent?>?) {
        this.listOfficeItems= responseContents!!
        this.listOfficeItems.add(InstitutionResponseContent())
        notifyDataSetChanged()
    }

    fun getlistDetails(): List<InstitutionResponseContent?> {
        return listOfficeItems

    }

    private class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.txtDropDownLabel) as TextView
    }
}
