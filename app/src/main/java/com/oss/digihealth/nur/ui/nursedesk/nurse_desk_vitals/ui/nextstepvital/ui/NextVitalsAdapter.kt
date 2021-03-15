package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.TemplateDetail
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.GetVital
import com.oss.digihealth.nur.ui.emr_workflow.vitals.ui.VitalSearchAdapter
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom.UOMNewReferernceResponseContent
import kotlin.math.pow


typealias OnClickVitalValueField = () -> Unit

class NextVitalsAdapter(
    private val context: Activity,
    private var vitalsList: ArrayList<TemplateDetail>,
    private val onClickVitalValueField: OnClickVitalValueField
) : RecyclerView.Adapter<NextVitalsAdapter.VitalHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var typeList = mutableMapOf<Int?, String?>()
    private val hashMapType: HashMap<Int, Int> = HashMap()
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var parentID = 0
    //var hashMapVitalsList : HashMap<Int,Int> = HashMap()

    private val hashtypeSpinnerList: HashMap<Int?, Int?> = HashMap()
    private var onSearchClickListener: OnSearchClickListener? = null

    private var vitalsListConst: ArrayList<TemplateDetail> = ArrayList<TemplateDetail>()

    private var textChanged = false



    var WeightPos: Int ?=null
    var HeightPos: Int ?=null
    var BMIPos: Int ?=null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VitalHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_vitals, parent, false)
        return VitalHolder(view)

    }

    override fun getItemCount(): Int {
        return vitalsList.size

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VitalHolder, position: Int) {
        val vitalsTemplateMasterDetail = vitalsList[position]
        holder.nameTextView.setText(vitalsTemplateMasterDetail.name)
        holder.spinner_type.isEnabled = false
        val pos = position + 1
        holder.serialNumberTextView.text = pos.toString()
        holder.deleteImageView.setOnClickListener {
            //onDeleteClickListener?.onDeleteClick(vitalsTemplateMasterDetail, position)

            deleteItem(position, vitalsTemplateMasterDetail.name)
        }
        holder.vitualEditText.setOnClickListener {
            if (!holder.vitualEditText.hasFocus())
                onClickVitalValueField.invoke()
        }




        if (vitalsTemplateMasterDetail.vital_value_type.code == "Numeric") {

            holder.vitualEditText.inputType = InputType.TYPE_CLASS_NUMBER

        } else if (vitalsTemplateMasterDetail.vital_value_type.code == "alphabet") {

            holder.vitualEditText.keyListener =
                DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

            holder.vitualEditText.inputType = InputType.TYPE_CLASS_TEXT

        } else {

            holder.vitualEditText.inputType = InputType.TYPE_CLASS_TEXT

        }

        holder.vitualEditText.setText(vitalsTemplateMasterDetail.vital_value)
        if (position == vitalsList.size - 1) {
            holder.deleteImageView.visibility = View.INVISIBLE
            holder.spinner_type.visibility = View.INVISIBLE
            holder.vitualEditText.visibility = View.INVISIBLE
        } else {
            holder.deleteImageView.visibility = View.VISIBLE
            holder.spinner_type.visibility = View.VISIBLE
            holder.vitualEditText.visibility = View.VISIBLE
        }

        //Fill EditText with the value you have in data source
        holder.vitualEditText.setText(vitalsList.get(position).vital_value)
        holder.vitualEditText.id = position

        //we need to update adapter once we finish with editing




        //bmi ,height, weight position

        if (vitalsList[position].name == "BMI" || vitalsList[position].name == "bmi" || vitalsList[position].name == "Bmi")
            BMIPos=position
        else if (vitalsList[position].name == "WEIGHT" || vitalsList[position].name == "Weight" || vitalsList[position].name == "weight")
            WeightPos=position

        else if (vitalsList[position].name == "HEIGHT" || vitalsList[position].name == "Height" || vitalsList[position].name == "height")
            HeightPos=position


        holder.vitualEditText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if (s.length > 0) {
                        vitalsList[position].vital_value = s.toString()


                    }
                } catch (e: java.lang.Exception) {

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {


                vitalsList[position].vital_value = s.toString()


                if (vitalsList.size > 3) {

                    var bmiCalcFinal: Double = 0.0

                    if (s.toString() != "") {

                        if ((HeightPos != null && WeightPos != null) && BMIPos != null) {


                            if (HeightPos == position) {


                                if (vitalsList[WeightPos!!].vital_value != "") {


                                    var bmiWeight: Int = vitalsList[WeightPos!!].vital_value.toInt()
                                    var bmiHeight: Int = vitalsList[HeightPos!!].vital_value.toInt()


                                    bmiCalcFinal =
                                        bmiWeight.toDouble() / bmiHeight.toDouble() / bmiHeight.toDouble() * 10000

                                    vitalsList[BMIPos!!].vital_value = bmiCalcFinal.toString()

                                    notifyItemChanged(BMIPos!!)

                                }
                                else{


                                    vitalsList[BMIPos!!].vital_value = ""

                                    notifyItemChanged(BMIPos!!)
                                }



                            } else if (WeightPos == position) {


                                if (vitalsList[HeightPos!!].vital_value != "") {

                                    var bmiWeight: Int = vitalsList[WeightPos!!].vital_value.toInt()
                                    var bmiHeight: Int = vitalsList[HeightPos!!].vital_value.toInt()


                                    bmiCalcFinal =
                                        bmiWeight.toDouble() / bmiHeight.toDouble() / bmiHeight.toDouble() * 10000

                                    vitalsList[BMIPos!!].vital_value = bmiCalcFinal.toString()

                                    notifyItemChanged(BMIPos!!)

                                }
                                else{

                                    vitalsList[BMIPos!!].vital_value = ""

                                    notifyItemChanged(BMIPos!!)
                                }


                            }

                        }


                    }
                }
            }
        })


        holder.nameTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 2) {

                    onSearchClickListener?.onSearchClick(
                        holder.nameTextView, position
                    )

                    textChanged = true
                }
            }
        })


        val adapter =
            ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                typeList.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner_type.adapter = adapter

        holder.spinner_type.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                val itemValue = parent?.getItemAtPosition(pos).toString()
                vitalsTemplateMasterDetail.uom_master_uuid =
                    typeList.filterValues { it == itemValue }.keys.toList()[0]!!

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                val itemValue = parent?.getItemAtPosition(pos).toString()
                vitalsTemplateMasterDetail.uom_master_uuid =
                    typeList.filterValues { it == itemValue }.keys.toList()[0]!!
                //Log.e("SpinnerSelec",vitalsList[position].uom_master_uuid.toString())
            }
        }

        try {

            holder.spinner_type.setSelection(hashtypeSpinnerList.get(vitalsTemplateMasterDetail.uom_master_uuid)!!)

        } catch (e: Exception) {
            Log.i("SpinnerMapErr", e.toString())
        }

        for (i in vitalsListConst.indices) {

            if (vitalsTemplateMasterDetail.uuid == vitalsListConst[i].uuid) {

                if (vitalsTemplateMasterDetail.uuid != 0) {
                    holder.nameTextView.isEnabled = false
                    holder.spinner_type.isClickable = false
                    holder.spinner_type.isEnabled = false
                    //holder.spinner_type.alpha = 0.5F
                } else {
                    holder.nameTextView.isEnabled = true
                    holder.spinner_type.isClickable = true
                    holder.spinner_type.isEnabled = true
                }

            } else {
                holder.nameTextView.isClickable = true
                holder.nameTextView.isFocusable = true
            }

        }

        /*if(vitalsTemplateMasterDetail.uuid == 0){
            holder.nameTextView.isClickable = true
            holder.nameTextView.isFocusable = true
            holder.nameTextView.isEnabled = true
        }*/

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            templateDetail: TemplateDetail,
            position: Int
        )
    }


    interface OnItemClickListener {
        fun onItemClick(
            templateDetail: TemplateDetail?,
            position: Int,
            selected: Boolean
        )
    }


    fun setOnSearchClickListener(onSearchClickListener: OnSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener
    }

    interface OnSearchClickListener {
        fun onSearchClick(
            autoCompleteTextView: AppCompatAutoCompleteTextView,
            position: Int
        )
    }


    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<GetVital>,
        searchPosition: Int?
    ) {
        val responseContentAdapter = VitalSearchAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as GetVital?

            val check = vitalsList.any { it.uuid == selectedPoi?.uuid }

            if (!check) {
                if (vitalsList[searchPosition!!].name == "BMI" && vitalsList.size > 3) {
                    var valueCalculateBMI = ""
                    val bmiValue: Int =
                        ((vitalsList[0].vital_value).toInt() / (vitalsList[0].vital_value.toDouble()
                            .pow(2.0)).toInt())
                    valueCalculateBMI = (bmiValue * 1000).toString()

                    vitalsList[searchPosition].vital_value = valueCalculateBMI
                }
                dropdownReferenceView.setText(selectedPoi?.name)
                vitalsList[searchPosition].name = selectedPoi!!.name
                vitalsList[searchPosition].uuid = selectedPoi.uuid
                vitalsList[searchPosition].uom_master_uuid = selectedPoi.uom_master_uuid
                vitalsList.add(TemplateDetail())
                notifyDataSetChanged()

            } else {
                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()
            }
        }
    }

    inner class VitalHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val nameTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.VitalName)
        internal val spinner_type: Spinner = itemView.findViewById(R.id.uomSpinner)
        internal var vitualEditText: EditText = itemView.findViewById(R.id.vitalEdittext)


    }

    fun addFavouritesInRow(templateMasterDetails: ArrayList<TemplateDetail>) {

        for (i in templateMasterDetails.indices) {

            val check = vitalsList.any { it.uuid == templateMasterDetails[i].uuid }

            if (!check) {

                if (templateMasterDetails.get(i).is_default) {
                    vitalsList.add(templateMasterDetails.get(i))
                    vitalsListConst.add(templateMasterDetails.get(i))
                }

                notifyDataSetChanged()
            } else {

                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()

            }
        }
        vitalsList.add(TemplateDetail())
        vitalsListConst.add(TemplateDetail())
        notifyDataSetChanged()
    }

    fun addRow(
        responseContent: TemplateDetail?

    ) {
        val check = vitalsList.any { it.uuid == responseContent?.uuid }

        if (!check) {
            vitalsList.removeAt(vitalsList.size - 1)
            vitalsList.add(responseContent!!)
            vitalsList.add(TemplateDetail())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    //Navigation template to list delete
    fun deleteRowItem(id: Int) {
        for (i in vitalsList.indices) {

            if (vitalsList.get(i).uuid.equals(id)) {
                this.vitalsList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int, name: String) {

        val customdialog = Dialog(context)
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog.setCancelable(false)
        customdialog.setContentView(R.layout.delete_cutsom_layout)
        val closeImageView = customdialog.findViewById(R.id.closeImageView) as ImageView
        closeImageView.setOnClickListener {
            customdialog.dismiss()
        }
        val drugNmae = customdialog.findViewById(R.id.addDeleteName) as TextView
        drugNmae.text = "${drugNmae.text} '" + name + "' record ?"

        val yesBtn = customdialog.findViewById(R.id.yes) as CardView
        val noBtn = customdialog.findViewById(R.id.no) as CardView
        yesBtn.setOnClickListener {
            try {
                if (!vitalsList.isNullOrEmpty() && vitalsList.size > 0) {

                    if (vitalsList.size == vitalsListConst.size) {
                        vitalsList.removeAt(position)
                        vitalsListConst.removeAt(position)
                    } else {
                        vitalsList.removeAt(position)
                    }
                    notifyDataSetChanged()
                }

                Toast.makeText(context, "$name Deleted Successfully", Toast.LENGTH_SHORT).show()
                customdialog.dismiss()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("deleteErr", e.toString())
            }
        }
        noBtn.setOnClickListener {
            customdialog.dismiss()
        }
        customdialog.show()

    }

    fun setTypeValue(responseContents: List<UOMNewReferernceResponseContent?>?) {

        typeList = responseContents!!.map { it!!.uuid to it.name }.toMap().toMutableMap()

        hashtypeSpinnerList.clear()

        for (i in responseContents.indices) {

            hashtypeSpinnerList[responseContents[i]!!.uuid] = i
        }

        notifyDataSetChanged()

    }

    fun getall(): ArrayList<TemplateDetail> {

        return this.vitalsList

    }

    fun clearAll() {

        vitalsList.clear()
        vitalsList = ArrayList<TemplateDetail>()

        notifyDataSetChanged()
    }


    fun addPrevRow(
        responseContent: TemplateDetail?

    ) {
        val check = vitalsList.any { it.uuid == responseContent?.uuid }

        if (!check) {
            if (vitalsList.size > 0) {
                vitalsList.removeAt(vitalsList.size - 1)
            }
            vitalsList.add(responseContent!!)
            vitalsList.add(TemplateDetail())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun clearAllData(getallVitals: List<TemplateDetail?>?) {


    }

}









