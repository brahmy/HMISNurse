package com.oss.digihealth.nur.ui.emr_workflow.vitals.ui
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchNameresponseContent


class TestNameSearchResultAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    private val allPois: List<VitalSearchNameresponseContent?>?
) :
    ArrayAdapter<VitalSearchNameresponseContent>(context, layoutResource, allPois!!),
    Filterable {
    private var mPois: List<VitalSearchNameresponseContent?>? = allPois

    override fun getCount(): Int {
        return mPois!!.size
    }

    override fun getItem(p0: Int): VitalSearchNameresponseContent? {
        return mPois!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return mPois!!.get(p0)!!.uuid?.toLong()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(layoutResource, parent, false)
        val name = view!!.findViewById<View>(R.id.nameTextView) as TextView
        val responseContent = getItem(position)
        name.text = responseContent!!.name

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                mPois = filterResults.values as List<VitalSearchNameresponseContent>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPois
                else
                    allPois!!.filter {
                        it!!.name?.toLowerCase()?.contains(queryString)!!
                    }
                return filterResults
            }
        }
    }
}