package com.oss.digihealth.nur.ui.emr_workflow.view.lab.ui

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
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.NurseDeskRadiologyChildFragmentBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskRadiologyViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.radiology.model.RecyclerDto
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse.NurseRadiologyResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse.PatientOrderTestDetail
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse.ResponseContent
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.postsample.NursedeskPostsampleResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.ui.NurseDeskRadiologyResultDialogFragment
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.view_model.NurseDeskRadiologyViewModel
import retrofit2.Response
import java.util.*


class NurdeDeskRadiologyChildFragment : Fragment() {

    private val TAG = NurdeDeskRadiologyChildFragment::class.java.simpleName

    private var binding: NurseDeskRadiologyChildFragmentBinding? = null
    private var viewModel: NurseDeskRadiologyViewModel? = null
    private var utils: Utils? = null
    private var patientUuid:Int = 0
    private var mAdapter: NurseDeskRadiologyParentAdapter? = null
    private val labParentList: MutableList<RecyclerDto> = ArrayList()
    private val customdialog:Dialog?=null
    var appPreferences : AppPreferences?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.nurse_desk_radiology_child_fragment,
                container,
                false
            )
        viewModel = NurseDeskRadiologyViewModelFactory(
            requireActivity().application
        ).create(NurseDeskRadiologyViewModel::class.java)
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
        val ward_UUID = appPreferences?.getInt(AppConstants.WARDUUID)
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        prepareLIstData()

        viewModel?.getRadilogyResultData(radiologyResultNurseRetrofitCallBack)

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
        binding?.labparentRecyclerView!!.layoutManager = layoutmanager
        mAdapter = NurseDeskRadiologyParentAdapter(requireActivity(),viewModel,customdialog,ArrayList(),radiologyResultNurseRetrofitCallBack)
        binding?.labparentRecyclerView!!.adapter = mAdapter

    }


    private val radiologyResultNurseRetrofitCallBack = object :
        RetrofitCallback<NurseRadiologyResponse?> {
        override fun onSuccessfulResponse(response: Response<NurseRadiologyResponse?>) {

            var s= response.body()?.responseContents!!.size

            if(s>0)
                binding?.norecord?.visibility=View.GONE
            else
                binding?.norecord?.visibility=View.VISIBLE

            mAdapter?.setData(response.body()?.responseContents)
        }

        override fun onBadRequest(response: Response<NurseRadiologyResponse?>) {
            val gson = GsonBuilder().create()
            val responseModel: NurseRadiologyResponse
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    NurseRadiologyResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
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
    class NurseDeskRadiologyParentAdapter(
        private val mContext: Context,
        private val viewModel: NurseDeskRadiologyViewModel?,
        private val customdialog: Dialog?,
        private var labParentList: List<ResponseContent?>? = ArrayList(),
        private val radiologyResultNurseRetrofitCallBack: RetrofitCallback<NurseRadiologyResponse?>

    ) : RecyclerView.Adapter<NurseDeskRadiologyParentAdapter.MyViewHolder>() {
        private val mLayoutInflater: LayoutInflater
        internal lateinit var orderNumString: String
        private var filter: List<ResponseContent?>? = ArrayList()

        init {
            this.mLayoutInflater = LayoutInflater.from(mContext)
        }

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val detailsview: TextView
            val labChildresultLinearLayout: LinearLayout
            val labParentLinearLayout: LinearLayout

            internal var recyclerView: RecyclerView

            init {

                detailsview = view.findViewById<View>(R.id.detailsview) as TextView
                labChildresultLinearLayout = view.findViewById(R.id.labChildresultLinearLayout)
                labParentLinearLayout = view.findViewById<View>(R.id.labParentLinearLayout) as LinearLayout
                recyclerView = view.findViewById(R.id.nurse_desk_investigation_child_recycler)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemLayout = LayoutInflater.from(mContext).inflate(
                R.layout.nurse_desk_radiology_parent_list,
                parent,
                false
            ) as CardView
            val recyclerView: RecyclerView
            return MyViewHolder(itemLayout)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            orderNumString = filter!!.get(position)?.toString()!!
            val prevList = filter!!.get(position)
            val result = if (prevList?.vw_patient_info?.gender_uuid==1) "Male" else "Female"
            holder.detailsview.text =prevList?.vw_patient_info?.pattitle+" / "+prevList?.vw_patient_info?.first_name+" / "+
                    prevList?.vw_patient_info?.ageperiod+" / "+result+" / "+prevList?.vw_patient_info?.uhid
            holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
            val myOrderChildAdapter = NurseDeskRadiologyChildAdapter(mContext,viewModel, customdialog,prevList?.patient_order_test_details,radiologyResultNurseRetrofitCallBack)
            val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
            holder.recyclerView.addItemDecoration(itemDecor)
            holder.recyclerView.adapter = myOrderChildAdapter

            holder.labParentLinearLayout.setOnClickListener {
                if (holder.labChildresultLinearLayout.visibility == View.VISIBLE) {
                    holder.labChildresultLinearLayout.visibility = View.GONE
                } else {
                    holder.labChildresultLinearLayout.visibility = View.VISIBLE
                }}}
        override fun getItemCount(): Int {
            return filter?.size!!
        }

        fun setData(responseContents: List<ResponseContent?>?) {
            filter = responseContents
            labParentList = responseContents
            notifyDataSetChanged()
        }
        fun getFilter(): Filter {
            return object : Filter() {
                @SuppressLint("DefaultLocale")
                override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                    val charString = charSequence.toString()
                    if (charString.isEmpty()) {
                        filter = labParentList
                    } else {
                        val filteredList = java.util.ArrayList<ResponseContent>()
                        for (messageList in labParentList!!) {
                            if (messageList?.vw_patient_info?.first_name != null) {
                                if (messageList.vw_patient_info?.first_name!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.pattitle != null) {
                                if (messageList.vw_patient_info?.pattitle!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.pattitle != null) {
                                if (messageList.vw_patient_info?.pattitle!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.ageperiod != null) {
                                if (messageList.vw_patient_info?.ageperiod!!.toLowerCase().contains(
                                        charString.toLowerCase()
                                    )
                                ) {
                                    filteredList.add(messageList)
                                }
                            }
                            if (messageList?.vw_patient_info?.uhid != null) {
                                if (messageList.vw_patient_info?.uhid!!.toLowerCase().contains(
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
                    filter = filterResults.values as java.util.ArrayList<ResponseContent>
                    notifyDataSetChanged()
                }
            }
        }
    }
    class NurseDeskRadiologyChildAdapter(
        private val mContext: Context,
        private val viewModel: NurseDeskRadiologyViewModel?,
        private var customdialog: Dialog?,
        private var labChildResult: List<PatientOrderTestDetail?>? = ArrayList(),
        private val radiologyResultNurseRetrofitCallBack: RetrofitCallback<NurseRadiologyResponse?>

    ) : RecyclerView.Adapter<NurseDeskRadiologyChildAdapter.MyViewHolder>() {
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
                R.layout.nurse_desk_radiology_child_recycler_list,
                viewGroup,
                false
            ) as ConstraintLayout
            return MyViewHolder(itemLayout)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val prevList = labChildResult?.get(position)
            val pos = position+1
            holder.labSNoTextView.text = pos.toString()
            holder.labNameText.text = prevList?.test_master?.name
            holder.labTypeText.text =prevList?.order_priority?.name
            val colorstatus = if(prevList?.is_nurse_collected!!) "Gray" else "Orange"
            if(prevList.order_status?.name?.equals("CREATED")!!)
            {
                if(colorstatus.equals("Gray"))
                {
                    holder.actionImage.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{

                    holder.actionImage.setImageResource(R.drawable.ic_order_process_orange)
                }
            }
            if(prevList.order_status?.name?.equals("ACCEPTED")!!)
            {
                holder.actionImage.setImageResource(R.drawable.orange_tick)
            }
            if(prevList.order_status?.name?.equals("APPROVED")!!)
            {
                if(colorstatus.equals("Gray"))
                {
                    holder.actionImage.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder.actionImage.setImageResource(R.drawable.ic_order_process_orange)
                }
            }

            if(prevList.order_status?.name?.equals("REJECTED")!!)
            {
                if(colorstatus.equals("Gray"))
                {
                    holder.actionImage.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder.actionImage.setImageResource(R.drawable.ic_close_red)
                }
            }



            if(prevList.order_status?.name?.equals("EXECUTED")!!)
            {
                if(colorstatus.equals("Gray"))
                {
                    holder.actionImage.setImageResource(R.drawable.ic_order_process_gray)
                }
                else{
                    holder.actionImage.setImageResource(R.drawable.green_tick)
                }
            }
            holder.actionImage.setOnClickListener({
                if(prevList.order_status?.name?.equals("CREATED")!!) {
                    if(colorstatus.equals("Orange")) {
                        //ClickListener
                        if(customdialog== null) {
                            customdialog = Dialog(mContext)
                            customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            customdialog!!.setCancelable(false)
                            customdialog!!.setContentView(R.layout.nurse_cutsom_alert_layout)
                            val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                            val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                            yesBtn.setOnClickListener {
                                viewModel?.getRadilogySampleCollection(prevList.uuid,radiologySampleCollectionNurseRetrofitCallBack)
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
            })
            holder.itemView.setOnClickListener {
                val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = NurseDeskRadiologyResultDialogFragment()
                val bundle = Bundle()
                bundle.putInt("UUID", prevList.uuid!!)
                dialog.arguments = bundle
                dialog.show(ft, "Tag")

            }}
        override fun getItemCount(): Int {
            return labChildResult?.size!!
        }

        private val radiologySampleCollectionNurseRetrofitCallBack = object : RetrofitCallback<NursedeskPostsampleResponse?> {
            override fun onSuccessfulResponse(response: Response<NursedeskPostsampleResponse?>) {
                // Reload current fragment
                // Reload current fragment
                Log.i("",""+response)
                Log.i("",""+response)
                Log.i("",""+response)

                viewModel?.getRadilogyResultData(radiologyResultNurseRetrofitCallBack)


            }

            override fun onBadRequest(response: Response<NursedeskPostsampleResponse?>) {
                val gson = GsonBuilder().create()
                val responseModel: NursedeskPostsampleResponse
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NursedeskPostsampleResponse::class.java
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


