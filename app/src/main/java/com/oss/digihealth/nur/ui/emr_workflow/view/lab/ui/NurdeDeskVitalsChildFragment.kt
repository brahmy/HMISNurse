package com.oss.digihealth.nur.ui.emr_workflow.view.lab.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.NurseDeskVitalsChildFragmentBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui.NextVitalsFragment
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskVitalsViewModelFactory
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response.NurseDeskVitalsResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.NurseDeskVitalsAdapter
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui.NextVitalChildFragment
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.viewmodel.NurseDeskVitalsViewModel
import retrofit2.Response
import java.util.*


class NurdeDeskVitalsChildFragment : Fragment() {
    private var binding: NurseDeskVitalsChildFragmentBinding? = null
    private var viewModel: NurseDeskVitalsViewModel? = null
    private var utils: Utils? = null
    private var mAdapter: NurseDeskVitalsAdapter? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var appPreferences: AppPreferences? = null
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.nurse_desk_vitals_child_fragment,
            container,
            false
        )
        viewModel = NurseDeskVitalsViewModelFactory(
            requireActivity().application
        ).create(NurseDeskVitalsViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)

        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        prepareLIstData()
        viewModel?.getNurseVitals(
            currentPage,
            pageSize,
            "",
            "",
            "",
            "",
            getNurseDeskRetrofitCallback
        )


        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.GONE) {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE
            } else {
                binding?.advanceSearchLayout?.visibility = View.GONE
            }
        }

        binding?.searchButton!!.setOnClickListener {
            val search = binding?.quickSearch?.text?.trim().toString()
            val pin = binding?.quickPin?.text?.trim().toString()
            val patientName = binding?.quickPatientName?.text?.trim().toString()
            val ipNumber = binding?.ipNumberEditText?.text?.trim().toString()
            if (search.isEmpty() && pin.isEmpty() && patientName.isEmpty() && ipNumber.isEmpty()) {
                Toast.makeText(activity, "Enter minimum 3 characters", Toast.LENGTH_LONG).show()
            } else {
                hideKeyboard(binding?.searchButton!!)
                currentPage = 0

                pageSize = 10

                viewModel?.getNurseVitals(
                    currentPage,
                    pageSize,
                    search,
                    pin,
                    patientName,
                    ipNumber,
                    getNurseDeskRetrofitCallback
                )
            }
        }
        binding?.clear!!.setOnClickListener {
            hideKeyboard(binding?.quickSearch!!)
            binding?.quickSearch?.setText("")
            binding?.quickPin?.setText("")
            binding?.quickPatientName?.setText("")
            binding?.ipNumberEditText?.setText("")
            currentPage = 0

            pageSize = 10

            viewModel?.getNurseVitals(
                currentPage,
                pageSize,
                "",
                "",
                "",
                "",
                getNurseDeskRetrofitCallback
            )
        }

        mAdapter?.setnextClickListerner(object : NurseDeskVitalsAdapter.nextClickListerner {
            override fun nextClick() {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.drawerLayout, NextVitalsFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

        })

        binding?.nurseVitalsRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            //Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            val search = binding?.quickSearch?.text?.trim().toString()
                            val pin = binding?.quickPin?.text?.trim().toString()
                            val patientName = binding?.quickPatientName?.text?.trim().toString()
                            val ipNumber = binding?.ipNumberEditText?.text?.trim().toString()
                            viewModel?.getNurseVitals(
                                currentPage,
                                pageSize,
                                search,
                                pin,
                                patientName,
                                ipNumber,
                                getNurseDeskSecondRetrofitCallback
                            )

                        }
                    }
                }
            }
        })

        return binding!!.root
    }


    private fun prepareLIstData() {
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.nurseVitalsRecyclerView!!.layoutManager = layoutmanager
        mAdapter = NurseDeskVitalsAdapter(requireActivity(), ArrayList()) {
            Log.e("clicked", "_______" + it.toString())

            val nxtFrag = NextVitalChildFragment()
            val args = Bundle()

            args.putString("prof_details", it.patient_title_name)

            args.putString("tName", it.patient_title_name)
            args.putString("fName", it.patient_first_name)
            args.putString("age", it.patient_age.toString())
            args.putString("gender", it.patient_gender_name)
            args.putString("pin", it.patient_uhid)
            args.putString("mobile", it.patient_mobile)
            args.putString("mobile", it.patient_mobile)
            args.putString("patientId", it.patients_uuid.toString())
            args.putString("patientInfo", it.patient_info.toString())
            args.putString(
                "admission_encounter_type_uuid",
                it.admission_encounter_type_uuid.toString()
            )
            args.putString("admission_encounter_uuid", it.admission_encounter_uuid.toString())

            nxtFrag.arguments = args
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.drawerLayout, nxtFrag, "tag")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        binding?.nurseVitalsRecyclerView!!.adapter = mAdapter
    }

    val getNurseDeskRetrofitCallback = object : RetrofitCallback<NurseDeskVitalsResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<NurseDeskVitalsResponseModel?>) {
            Log.e("VitalsResponse", responseBody?.body()?.responseContents.toString())
//            mAdapter?.setData(responseBody?.body()?.responseContents)
            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                val patientCount = responseBody.body()?.totalRecords.toString() ?: "0"
                binding?.txtPatientCount?.text = patientCount
                TOTAL_PAGES =
                    Math.ceil(responseBody.body()!!.totalRecords!!.toDouble() / 10).toInt()
                if (responseBody.body()!!.responseContents!!.isNotEmpty()) {
                    mAdapter?.clearAll()
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.addAll(responseBody.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        isLoadingPaginationAdapterCallback = false
                        //   binding?.progressbar!!.setVisibility(View.VISIBLE);
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        //    binding?.progressbar!!.setVisibility(View.GONE);
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }


                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
                if (responseBody.body()!!.totalRecords!! < 11) {
                    binding?.progressbar!!.visibility = View.GONE
                }
            } else {
                binding?.txtPatientCount?.text = "0"
                Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show()
                mAdapter!!.clearAll()
                binding?.progressbar!!.visibility = View.GONE
            }
            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
        }

        override fun onBadRequest(response: Response<NurseDeskVitalsResponseModel?>) {
            val gson = GsonBuilder().create()
            var responseModel: NurseDeskVitalsResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    NurseDeskVitalsResponseModel::class.java
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


    val getNurseDeskSecondRetrofitCallback =
        object : RetrofitCallback<NurseDeskVitalsResponseModel?> {
            override fun onSuccessfulResponse(responseBody: Response<NurseDeskVitalsResponseModel?>) {
                Log.e("VitalsResponseSec", responseBody?.body()?.responseContents.toString())
//            mAdapter?.setData(responseBody?.body()?.responseContents)
                if (responseBody!!.body()?.responseContents!!.isNotEmpty()) {

                    binding?.progressbar!!.visibility = View.GONE

                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLoadingPaginationAdapterCallback = false
                    mAdapter?.addAll(responseBody.body()!!.responseContents)
                    println("testing for two  = $currentPage--$TOTAL_PAGES")
                    if (currentPage < TOTAL_PAGES) {
                        //   binding?.progressbar!!.setVisibility(View.VISIBLE);
                        mAdapter?.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                        println("testing for four  = $currentPage--$TOTAL_PAGES")
                    } else {
                        isLastPage = true
//                    visitHistoryAdapter.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                        println("testing for five  = $currentPage--$TOTAL_PAGES")
                    }
                } else {
                    println("testing for six  = $currentPage--$TOTAL_PAGES")
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter?.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
            }

            override fun onBadRequest(response: Response<NurseDeskVitalsResponseModel?>) {
                binding?.progressbar!!.visibility = View.GONE
                isLoadingPaginationAdapterCallback = false
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true
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
                binding?.progressbar!!.visibility = View.GONE
            }

        }

    fun hideKeyboard(it: View) {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }


}


