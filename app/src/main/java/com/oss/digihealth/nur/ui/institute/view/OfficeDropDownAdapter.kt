package com.oss.digihealth.nur.ui.institute.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseContent

class OfficeDropDownAdapter(
    val context: Context,
    var listOfficeItems: ArrayList<OfficeResponseContent?>
) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.view_drop_down_office, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        val params = view.layoutParams
        //params.height = 60
        view.layoutParams = params
        if (listOfficeItems[position]!!.health_office?.office_name.equals("")) {
            vh.label.hint = "Please select Office"

        } else {
            vh.label.text = listOfficeItems.get(position)!!.health_office?.office_name
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

    fun setOfficeListDetails(responseContents: ArrayList<OfficeResponseContent?>?) {
        this.listOfficeItems = responseContents!!
        this.listOfficeItems.add(OfficeResponseContent())
        notifyDataSetChanged()
    }

    fun getlistDetails(): List<OfficeResponseContent?> {
        return listOfficeItems

    }

    private class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.txtDropDownLabel) as TextView
    }
}
