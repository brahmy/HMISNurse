package com.oss.digihealth.nur.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.LabSearchResultAdapter
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.LabTypeResponseContent
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.favresponse.FavSearch
import com.oss.digihealth.nur.ui.emr_workflow.model.favorite.FavouritesModel
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.TempDetails
import com.oss.digihealth.nur.ui.emr_workflow.radiology.model.ToLocation
import com.oss.digihealth.nur.ui.quick_reg.model.ResponseTestMethodContent

typealias OnStatusChanged = (favortemstatus:Int,position: Int, selected: Boolean) -> Unit

class LabAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>,
    val onStatusChanged: OnStatusChanged
) : RecyclerView.Adapter<LabAdapter.MyViewHolder>() {

    private var totalitem: Boolean?=false
    private val hashMapType: HashMap<Int, Int> = HashMap()
    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()
    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    lateinit var selectedResponseContent: FavouritesModel
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onstatus: OnstatuschangeListener? = null
    private var onListItemClickListener: OnListItemClickListener? = null
    var hashMapLabList: HashMap<Int, Int> = HashMap()
    private var testMethodMap = mutableMapOf<Int, String>()
    private var testMethodArrayList: List<ResponseTestMethodContent?>? = listOf()
    private val hashMaptestMethodMap: HashMap<Int, Int> = HashMap()

    private var onitemChangeListener: OnitemChangeListener? = null
    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()





    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_lab, parent, false)


        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = favouritesArrayList[position]!!
        hashMapLabList.put(
            favouritesArrayList[position]!!.test_master_id!!,
            favouritesArrayList[position]!!.template_id
        )

        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(selectedResponseContent.itemAppendString, false)

        if (favouritesArrayList[position]!!.test_master_code != null) {
            holder.codeTextView.setText(favouritesArrayList[position]!!.test_master_code.toString())
        } else {

            holder.codeTextView.setText("")
        }
        holder.commentsImageView.setOnClickListener {
            onCommandClickListener!!.onCommandClick(
                position,
                favouritesArrayList.get(position)!!.commands
            )
        }

        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView, position,
                        holder.spinnerToLocation
                    )
                }
            }
        })
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
        try {
            if (hashMapType.containsKey(selectedResponseContent.test_master_id!!)) {
                holder.spinner_type.setSelection(hashMapType.get(selectedResponseContent.test_master_id!!)!!)
            }
        } catch (e: Exception) {

        }



        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }
        holder.deleteImageView.setOnClickListener {

            selectedResponseContent = favouritesArrayList[position]!!

            if (!holder.autoCompleteTextView.text.trim().isEmpty()) {
                onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                hashMapType.remove(selectedResponseContent.test_master_id)

            }
            getIdList()

        }

        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false
            holder.commentsImageView.alpha = 0.2f
            holder.commentsImageView.isEnabled = false
            holder.commentsImageView.setFocusable(true);
            holder.commentsImageView.setFocusableInTouchMode(true);
            holder.autoCompleteTextView.setFocusable(true);
            holder.autoCompleteTextView.setFocusableInTouchMode(true);

            holder.autoCompleteTextView.requestFocus();
        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true
            holder.commentsImageView.alpha = 1f
            holder.commentsImageView.isEnabled = true
            holder.commentsImageView.setFocusable(false);
            holder.commentsImageView.setFocusableInTouchMode(false);
            holder.autoCompleteTextView.setFocusable(false);
            holder.autoCompleteTextView.setFocusableInTouchMode(false);
        }


        val adapterTestMethod =
            ArrayAdapter<String>(
                this.context,
                R.layout.spinner_item,
                testMethodMap.values.toMutableList()
            )
        adapterTestMethod.setDropDownViewResource(R.layout.spinner_item)
        holder.spinnerTestMethod.adapter = adapterTestMethod


        holder.spinnerTestMethod.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedResponseContent = favouritesArrayList[position]!!
                    selectedResponseContent.selectToTestMethodUUID =
                        testMethodMap.filterValues { it == itemValue }.keys.toList()[0]
                }

            }

        if (favouritesArrayList!![position]!!.TestMethodId != null) {
            val check3 =
                testMethodMap.any { it.key == favouritesArrayList[position]!!.TestMethodId }

            if (check3) {

                holder.spinnerTestMethod.setSelection(hashMaptestMethodMap[favouritesArrayList[position]!!.TestMethodId]!!)
            } else {
                holder.spinnerTestMethod.setSelection(0)
            }
        }


        val adapterType =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                typeNamesList.values.toMutableList()
            )
        adapterType.setDropDownViewResource(R.layout.spinner_item)
        holder.spinner_type.adapter = adapterType


        holder.spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                selectedResponseContent = favouritesArrayList[position]!!
                selectedResponseContent.selectTypeUUID =
                    typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                hashMapType.put(selectedResponseContent.test_master_id!!, pos)

            }

        }
        val locationAdapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                toLocationMap.values.toMutableList()
            )
        locationAdapter.setDropDownViewResource(R.layout.spinner_item);
        holder.spinnerToLocation.adapter = locationAdapter

        /*
            try {
                if (hashMapOrderToLocation.containsKey(selectedResponseContent.test_master_id)) {
                    holder.spinnerToLocation.setSelection(
                        hashMapOrderToLocation.get(
                            selectedResponseContent.test_master_id
                        )!!
                    );
                }
            } catch (e: Exception) {

            }*/
        if (favouritesArrayList!![position]!!.selectToLocationUUID != null) {
            val check3 =
                toLocationMap.any { it.key == favouritesArrayList[position]!!.selectToLocationUUID }

            if (check3) {

                holder.spinnerToLocation.setSelection(hashMapOrderToLocation[favouritesArrayList[position]!!.selectToLocationUUID]!!)
            } else {
                holder.spinnerToLocation.setSelection(0)
            }
        }


        holder.spinnerToLocation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedResponseContent = favouritesArrayList[position]!!
                    selectedResponseContent.selectToLocationUUID =
                        toLocationMap.filterValues { it == itemValue }.keys.toList()[0]
                    //    hashMapOrderToLocation.put(selectedResponseContent.test_master_id!!,pos)
                }

            }





    }

    override fun getItemCount(): Int {
        return favouritesArrayList!!.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.autoCompleteTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val spinner_type: Spinner = itemView.findViewById(R.id.type_spinner)
        internal val spinnerToLocation: Spinner = itemView.findViewById(R.id.tolocation)
        internal val spinnerTestMethod: Spinner = itemView.findViewById(R.id.testmethod)
        internal val codeTextView: TextView = itemView.findViewById(R.id.codeTextView)
        internal val commentsImageView: ImageView = itemView.findViewById(R.id.commentsImageView)
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int,
            spinnerToLocation: Spinner
        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun clearall() {

        favouritesArrayList.clear()
        notifyDataSetChanged()

    }


    fun clearallAddone() {
        favouritesArrayList.clear()
        favouritesArrayList.add(FavouritesModel())
        notifyDataSetChanged()
    }

    fun deleteRow(
        position1: Int
    ): Boolean {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

        if(favouritesArrayList!![position1]?.isModifiy!!) {
            removedListFromOriginal?.add(favouritesArrayList!![position1])
        }

        val data = favouritesArrayList[position1]

        this.favouritesArrayList.removeAt(position1)

        var ischeck: Boolean = true


        for (i in this.favouritesArrayList.indices) {
            if (favouritesArrayList[i]!!.template_id == data!!.template_id) {
                ischeck = false
                break
            }
        }
        notifyItemRemoved(position1);
        notifyDataSetChanged()
        return ischeck
    }

    /*
    Delete row from template
    */
    fun deleteRowFromTemplate(
        tempId: Int?,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        val OriginalItemCount = itemCount
        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1);
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.test_master_id?.equals(tempId!!)!! && favouritesArrayList.get(
                    i
                )?.viewLabstatus?.equals(position1!!)!!
            ) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i);
                break
            }

        }
        getIdList()
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }


    fun checkAlreadyPresent(testMasterUuid: Int?): Boolean{
        val check =
            favouritesArrayList?.any { it!!.test_master_id == testMasterUuid }
        return !check!!
    }

    fun addFavouritesInRowModule(favortemstatus:Int,
                                 responseContent: FavouritesModel?,
                                 position: Int,
                                 selected: Boolean
    ) {
        if(favortemstatus==1)
        {
            val check =
                favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check) {
                favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList.add(responseContent)
                favouritesArrayList.add(FavouritesModel())
                getIdList()
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()
                onStatusChanged.invoke(favortemstatus,position, true)
                notifyDataSetChanged()

            }
        }else{
            val check =
                favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check) {
                favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList.add(responseContent)
                favouritesArrayList.add(FavouritesModel())
                getIdList()
                notifyDataSetChanged()
                totalitem=true
            } else {
                if(totalitem!!)
                {
                    Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                        ?.show()
                    onStatusChanged.invoke(favortemstatus,position, false)
                    notifyDataSetChanged()
                }
                else{
                    Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                        ?.show()
                    onStatusChanged.invoke(favortemstatus,position, true)
                    notifyDataSetChanged()
                }



            }
        }


    }

    fun addSaveTemplateInRow(
        responseContent: FavouritesModel?
    ) {


        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList.add(responseContent)
            favouritesArrayList.add(FavouritesModel())
            notifyDataSetChanged()
        } else {

            notifyDataSetChanged()

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()

        }
    }

    fun addRow(
        responseContent: FavouritesModel?

    ) {
        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check) {
            favouritesArrayList.add(responseContent)
            notifyItemInserted(favouritesArrayList.size - 1)
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun addTempleteRow(
        responseContent: TempDetails?
    ) {
        templeteArrayList.add(responseContent)
        notifyItemInserted(templeteArrayList.size - 1)
    }

    fun setDuration(durationArrayList_: ArrayList<DurationResponseContent?>) {
        this.durationArrayList = durationArrayList_
        notifyDataSetChanged()
    }

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<FavSearch>,
        searchposition: Int,
        spinnerToLocation: Spinner
    ) {
        val responseContentAdapter = LabSearchResultAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents,
            true
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()

        dropdownReferenceView.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?


            val check = favouritesArrayList.any { it!!.test_master_id == selectedPoi?.uuid }

            if (!check) {

                dropdownReferenceView.setText(selectedPoi?.name)
                favouritesArrayList[searchposition]!!.chief_complaint_name = selectedPoi?.name
                favouritesArrayList[searchposition]!!.itemAppendString = selectedPoi?.name
                favouritesArrayList[searchposition]!!.test_master_id = selectedPoi?.uuid
                favouritesArrayList[searchposition]!!.test_master_code = selectedPoi?.code
                favouritesArrayList[searchposition]!!.test_master_name = selectedPoi?.name
                onListItemClickListener!!.onListItemClick(
                    favouritesArrayList[searchposition],
                    searchposition,
                    spinnerToLocation
                )
                if (selectedPoi?.type_of_method_uuid != null) {
                    favouritesArrayList[searchposition]!!.TestMethodId =
                        selectedPoi.type_of_method_uuid

                }
                notifyDataSetChanged()
                getIdList()
                addRow(FavouritesModel())
            } else {

                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()

            }

        }
    }

    fun setadapterTypeValue(responseContents: List<LabTypeResponseContent?>?) {
        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        notifyDataSetChanged()
    }

    fun setToLocationList(responseContents: List<LabToLocationResponse.LabToLocationContent?>?) {
        toLocationMap =
            responseContents?.map { it?.uuid!! to it.location_name!! }!!.toMap().toMutableMap()

        hashMapOrderToLocation.clear()

        for (i in responseContents.indices) {

            hashMapOrderToLocation[responseContents[i]!!.uuid!!] = i
        }

        notifyDataSetChanged()

    }

    fun getItems(): ArrayList<FavouritesModel?> {
        return favouritesArrayList
    }

    fun getAll(): ArrayList<FavouritesModel?> {

        return this!!.favouritesArrayList

    }

    fun addPrevInRow(
        responseContent: FavouritesModel?
    ) {


        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check) {
//            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList.add(responseContent)
            favouritesArrayList.add(FavouritesModel())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }
    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }
    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }
    /*
    onstatuslister
     */
    interface OnstatuschangeListener {
        fun onStatuschange(position: Int, selected: Boolean)
    }

    fun addCommands(position: Int, command: String) {
        favouritesArrayList[position]!!.commands = command
        notifyDataSetChanged()
    }

    interface OnListItemClickListener {
        fun onListItemClick(
            responseContent: FavouritesModel?,
            position: Int,
            spinnerToLocation: Spinner
        )
    }

    fun setOnListItemClickListener(onListItemClickListener: OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }

    fun setToLocation(
        responseContents: ToLocation?,
        spinnerToLocation: Spinner,
        searchPosition: Int?
    ) {
        val check =
            this.hashMapOrderToLocation.any { it!!.key == responseContents?.to_location_uuid }
        if (check) {
            spinnerToLocation.setSelection(hashMapOrderToLocation[responseContents?.to_location_uuid]!!)
            this.favouritesArrayList[searchPosition!!]!!.selectToLocationUUID =
                responseContents?.to_location_uuid!!
        }
    }

    fun setadapterTestMethodValue(responseContents: List<ResponseTestMethodContent?>?) {
        testMethodArrayList = responseContents
        testMethodMap = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        for (i in responseContents.indices) {
            hashMaptestMethodMap[responseContents[i]?.uuid!!] = i
        }
        notifyDataSetChanged()
    }

    fun addFavouritesInRow(
        responseContent: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList.add(responseContent)
            favouritesArrayList.add(FavouritesModel())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }}

    fun getIdList(){

        var sendListId:ArrayList<Int> = ArrayList()
        for (i in favouritesArrayList?.indices!!){

            sendListId.add(favouritesArrayList!![i]?.test_master_id!!)
        }

        onitemChangeListener?.onitemChangeClick(sendListId)

    }


    interface OnitemChangeListener {
        fun onitemChangeClick(
            uuid: ArrayList<Int>
        )
    }

    fun setOnitemChangeListener(onCommandClickListener: OnitemChangeListener) {
        this.onitemChangeListener = onCommandClickListener
    }

    fun getRemovedItems(): ArrayList<FavouritesModel?>?{
        return  removedListFromOriginal
    }

}


