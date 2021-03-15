package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.ui

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.NurseDeskInvestigationChildFragmentBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskInvestigationViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.radiology.model.RecyclerDto
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResponseContent
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseInvestigationPostResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.PatientOrderTestDetail
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.view_model.NurseDeskInvestigationViewModel
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import org.json.JSONException
import retrofit2.Response
import java.util.ArrayList


class NurdeDeskInvestigationChildFragment : Fragment() {

    private val TAG = NurdeDeskInvestigationChildFragment::class.java.simpleName

    private var binding: NurseDeskInvestigationChildFragmentBinding? = null
    private var viewModel: NurseDeskInvestigationViewModel? = null
    private var utils: Utils? = null
    private var patientUuid:Int = 0
    private var mAdapter: NurseDeskInvestigationParentAdapter? = null
    private val customdialog:Dialog?=null

    private val labParentList: MutableList<RecyclerDto> = ArrayList()

    var appPreferences : AppPreferences?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.nurse_desk_investigation_child_fragment,
                container,
                false
            )
        viewModel = NurseDeskInvestigationViewModelFactory(
            requireActivity().application
        ).create(NurseDeskInvestigationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)

        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        viewModel?.getNurseDeskInvestigationDetails(getInvestigationRetrofitCallBack)

        prepareLIstData()
        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
                binding?.advanceSearchLayout?.visibility = View.GONE
            } else {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE
            }
        }

        val searchText =
            binding?.searchView?.findViewById(R.id.search_src_text) as TextView
        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText.typeface = tf
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                mAdapter!!.getFilter().filter(query)
            }

        })

        return binding!!.root
    }


    private fun prepareLIstData() {

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.investigationparentRecyclerView!!.layoutManager = layoutmanager
        mAdapter = NurseDeskInvestigationParentAdapter(requireActivity(),viewModel,customdialog,ArrayList(),getInvestigationRetrofitCallBack)
        binding?.investigationparentRecyclerView!!.adapter = mAdapter

    }
    val getInvestigationRetrofitCallBack =
        object : RetrofitCallback<NurseDeskInvestigationResponseModel?> {
            override fun onSuccessfulResponse(response: Response<NurseDeskInvestigationResponseModel?>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    mAdapter?.refreshList(response.body()?.responseContents!!)

                }
            }
            override fun onBadRequest(response: Response<NurseDeskInvestigationResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: NurseDeskInvestigationResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NurseDeskInvestigationResponseModel::class.java
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String?) {
                if(failure!=null)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    class NurseDeskInvestigationParentAdapter(
        private val mContext: Context,
        private val viewModel: NurseDeskInvestigationViewModel?,
        private val customdialog: Dialog?,
        private var labParentList: List<NurseDeskInvestigationResponseContent>? = ArrayList(),
        private val investigationResultNurseRetrofitCallBack: RetrofitCallback<NurseDeskInvestigationResponseModel?>


    ) : RecyclerView.Adapter<NurseDeskInvestigationParentAdapter.MyViewHolder>() {
        private val mLayoutInflater: LayoutInflater
        internal lateinit var orderNumString: String

        private var filter: List<NurseDeskInvestigationResponseContent?>? = ArrayList()
        init {
            this.mLayoutInflater = LayoutInflater.from(mContext)
        }

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val dateTextView: TextView
            val investigationChildresultLinearLayout: LinearLayout
            val investigationParentLinearLayout: LinearLayout

            internal var recyclerView: RecyclerView

            init {

                dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
                investigationChildresultLinearLayout = view.findViewById(R.id.investigationChildresultLinearLayout)
                investigationParentLinearLayout = view.findViewById<View>(R.id.investigationParentLinearLayout) as LinearLayout
                recyclerView = view.findViewById(R.id.nurse_desk_investigation_child_recycler)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemLayout = LayoutInflater.from(mContext).inflate(
                R.layout.nurse_desk_investigation_parent_list,
                parent,
                false
            ) as CardView
            val recyclerView: RecyclerView
            return MyViewHolder(itemLayout)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            try {
                orderNumString = filter?.get(position).toString()
            }
            catch (e: JSONException) {
                e.printStackTrace()
            }
            val prevList = filter?.get(position)
            val result = if (prevList?.vw_patient_info?.gender_uuid==1) "Male" else "Female"
            holder.dateTextView.text = prevList?.vw_patient_info?.pattitle+" / "+prevList?.vw_patient_info?.first_name+" / "+prevList?.vw_patient_info?.ageperiod+"/"+result+" / "+prevList?.vw_patient_info?.uhid
            holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
            val myOrderChildAdapter = NurseDeskInvestigationChildAdapter(mContext, customdialog, viewModel, prevList?.patient_order_test_details,investigationResultNurseRetrofitCallBack)
            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)
            holder.recyclerView.adapter = myOrderChildAdapter


            holder.investigationParentLinearLayout.setOnClickListener {

                if (holder.investigationChildresultLinearLayout.visibility == View.VISIBLE) {
                    holder.investigationChildresultLinearLayout.visibility = View.GONE

                } else {
                    holder.investigationChildresultLinearLayout.visibility = View.VISIBLE
                }
            }


        }


        override fun getItemCount(): Int {
            return filter?.size!!
        }

        fun refreshList(preLabArrayList: List<NurseDeskInvestigationResponseContent>?) {
            filter = preLabArrayList
            this.labParentList = preLabArrayList!!
            this.notifyDataSetChanged()
        }

        fun getFilter(): Filter {

            return object : Filter() {
                @SuppressLint("DefaultLocale")
                override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                    val charString = charSequence.toString()

                    if (charString.isEmpty()) {

                        filter = labParentList
                    } else {
                        val filteredList = java.util.ArrayList<NurseDeskInvestigationResponseContent>()
                        for (messageList in labParentList!!) {
                            if (messageList?.vw_patient_info?.pattitle != null) {
                                if (messageList?.vw_patient_info?.pattitle!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.first_name != null) {
                                if (messageList?.vw_patient_info?.first_name!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.pattitle != null) {
                                if (messageList?.vw_patient_info?.pattitle!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.ageperiod != null) {
                                if (messageList?.vw_patient_info?.ageperiod!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.uhid != null) {
                                if (messageList?.vw_patient_info?.uhid!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                        }
                        filter = filteredList
                    }
                    val filterResults = Filter.FilterResults()
                    filterResults.values = filter
                    return filterResults
                }
                override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: Filter.FilterResults
                ) {
                    filter = filterResults.values as java.util.ArrayList<NurseDeskInvestigationResponseContent>
                    notifyDataSetChanged()
                }
            }
        }


    }

    class NurseDeskInvestigationChildAdapter(

        private val mContext: Context,
        private var customdialog: Dialog?,
        private val viewModel: NurseDeskInvestigationViewModel?,
        private var labChildResult: List<PatientOrderTestDetail>? = ArrayList(),
        private val investigationResultNurseRetrofitCallBack: RetrofitCallback<NurseDeskInvestigationResponseModel?>





    ) : RecyclerView.Adapter<NurseDeskInvestigationChildAdapter.MyViewHolder>() {
        private val mLayoutInflater: LayoutInflater


        init {
            this.mLayoutInflater = LayoutInflater.from(mContext)

        }


        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var labSNoTextView: TextView
            var labNameText: TextView
            var labTypeText: TextView
            lateinit var actionImage: ImageView


            init {

                labSNoTextView = view.findViewById<View>(R.id.labSNoTextView) as TextView
                labNameText = view.findViewById<View>(R.id.labNameText) as TextView
                labTypeText = view.findViewById<View>(R.id.labTypeText) as TextView
                actionImage = view.findViewById<View>(R.id.actionImage) as ImageView



            }
        }

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): MyViewHolder {
            val itemLayout = LayoutInflater.from(mContext).inflate(
                R.layout.nurse_desk_investigation_child_recycler_list,
                viewGroup,
                false
            ) as ConstraintLayout
            return MyViewHolder(itemLayout)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val prevList = labChildResult?.get(position)
            var pos = position+1
            holder.labSNoTextView.text = pos.toString()
            holder.labNameText.text = prevList?.test_master?.description
            holder.labTypeText.text = prevList?.test_master?.value_type_uuid.toString()
            var colorstatus = if(prevList?.is_nurse_collected!!) "Gray" else "Orange"

            if(prevList?.order_status?.name?.equals("CREATED")!!)
            {
                if(colorstatus.equals("Gray")!!)
                {
                    holder?.actionImage?.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder?.actionImage?.setImageResource(R.drawable.ic_order_process_orange)
                }
            }
            if(prevList?.order_status?.name?.equals("ACCEPTED")!!)
            {
                if(colorstatus.equals("Gray")!!)
                {
                    holder?.actionImage?.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder?.actionImage?.setImageResource(R.drawable.orange_tick)
                }
            }
            if(prevList?.order_status?.name?.equals("APPROVED")!!)
            {
                if(colorstatus.equals("Gray")!!)
                {
                    holder?.actionImage?.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder?.actionImage?.setImageResource(R.drawable.orange_tick)
                }
            }
            if(prevList.order_status?.name?.equals("EXECUTED")!!)
            {
                if(colorstatus.equals("Gray")!!)
                {
                    holder.actionImage?.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder.actionImage?.setImageResource(R.drawable.green_tick)
                }
            }
            if(prevList.order_status?.name?.equals("SENDFORAPPROVAL")!!)
            {
                holder.actionImage?.setImageResource(R.drawable.ic_order_process_gray)

            }
            if(prevList?.order_status?.name?.equals("REJECTED")!!)
            {
                if(colorstatus.equals("Gray")!!)
                {
                    holder.actionImage?.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder.actionImage.setImageResource(R.drawable.ic_close_red)
                }
            }


            holder?.actionImage?.setOnClickListener {
                if(prevList?.order_status?.name?.equals("CREATED")!!) {
                    if(colorstatus.equals("Orange")!!) {
                        //ClickListener
                        if(customdialog== null) {
                            customdialog = Dialog(mContext)
                            customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            customdialog!!.setCancelable(false)
                            customdialog!!.setContentView(R.layout.nurse_cutsom_alert_layout)
                            val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                            val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                            yesBtn.setOnClickListener {

                                viewModel?.getInvestigationSampleCollection(prevList.uuid,investigationSampleCollectionNurseRetrofitCallBack)

                                customdialog!!.dismiss()
                                customdialog = null
                            }
                            noBtn.setOnClickListener {
                                customdialog!!.dismiss()
                                customdialog = null
                            }
                            if (customdialog != null) {
                                customdialog!!.show()
                            }
                        }
                    }

                }
            }






            holder.itemView.setOnClickListener {


                val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = NurseDeskInvestigationResultDialogFragment()
                val bundle = Bundle()
                bundle.putInt("UUID", prevList?.uuid!!)
                dialog.arguments = bundle
                dialog.show(ft, "Tag")

            }


        }

        override fun getItemCount(): Int {
            return labChildResult?.size!!
        }

        private val investigationSampleCollectionNurseRetrofitCallBack = object : RetrofitCallback<NurseInvestigationPostResponse?> {
            override fun onSuccessfulResponse(response: Response<NurseInvestigationPostResponse?>) {
                // Reload current fragment
                // Reload current fragment
                Log.i("",""+response)
                Log.i("",""+response)
                Log.i("",""+response)

                viewModel?.getInvestigationResultData(investigationResultNurseRetrofitCallBack)


            }

            override fun onBadRequest(response: Response<NurseInvestigationPostResponse?>) {
                val gson = GsonBuilder().create()
                val responseModel: NurseInvestigationPostResponse
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NurseInvestigationPostResponse::class.java
                    )

                } catch (e: Exception) {

                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>?) {

            }

            override fun onUnAuthorized() {

            }

            override fun onForbidden() {

            }
            override fun onFailure(failure: String?) {

            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    }









}


