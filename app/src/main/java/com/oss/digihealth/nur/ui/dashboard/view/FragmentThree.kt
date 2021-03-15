package com.oss.digihealth.nur.ui.dashboard.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.ui.dashboard.model.Diagnosis
import com.oss.digihealth.nur.ui.dashboard.view_model.ZeroStockDashBoardViewModel
import com.oss.digihealth.nur.ui.dashboard.view_model.ZeroStockDashBoardViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.ZeroStockResponseModel
import kotlinx.android.synthetic.main.fragment_patients_complients.view.*
import retrofit2.Response

class FragmentThree : Fragment(){
    private var zeroStockAdapter: ZeroStockAdapter? = null

    private var viewModel: ZeroStockDashBoardViewModel? = null

    val data: ArrayList<Diagnosis>?
        get() = if (arguments == null) null else requireArguments().getSerializable(FragmentOne.ARG_NAME) as ArrayList<Diagnosis>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patients_complients, container, false)
        viewModel = ZeroStockDashBoardViewModelFactory(
            requireActivity().application
        )   .create(ZeroStockDashBoardViewModel::class.java)
        viewModel?.getZeroStock( zeroStockCallBack)
        return  view
    }



    companion object {
        const val ARG_NAME = "name"


        fun newInstance(data: ArrayList<Diagnosis>): FragmentThree {
            val fragment = FragmentThree()

            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, data)
            }

            fragment.arguments = bundle

            return fragment
        }
    }


    val zeroStockCallBack = object : RetrofitCallback<ZeroStockResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<ZeroStockResponseModel?>) {
            if(responseBody?.body()?.responseContents?.isNotEmpty()!!) {
                view?.rvPatientsComplients?.visibility = View.VISIBLE
                view?.tv_no_data_avaliable?.visibility = View.GONE
                Log.e("zerStock", responseBody?.body()?.responseContents.toString())
                zeroStockAdapter =
                    responseBody?.body()?.responseContents?.let {
                        ZeroStockAdapter(
                            requireContext(),
                            it
                        )
                    }
                view?.rvPatientsComplients?.layoutManager = LinearLayoutManager(requireActivity())
                view?.rvPatientsComplients?.adapter = zeroStockAdapter
            }else{
                noDataFound()
            }
        }

        override fun onBadRequest(errorBody: Response<ZeroStockResponseModel?>) {
            noDataFound()
            val gson = GsonBuilder().create()
            val responseModel: ZeroStockResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ZeroStockResponseModel::class.java
                )

                Log.e("zerStock", "BadRequest" + responseModel.message!!)

            } catch (e: Exception) {
                Log.e("zerStock", "BadRequest")
                e.printStackTrace()
            }

            // Log.e("postAllergyData", "BadRequest")

        }

        override fun onServerError(response: Response<*>?) {
            noDataFound()

            Log.e("zerStock", "server")
        }

        override fun onUnAuthorized() {
            noDataFound()
            Log.e("zerStock", "UnAuth")
        }

        override fun onForbidden() {
            noDataFound()
            Log.e("postAllergyData", "ForBidd")
        }

        override fun onFailure(s: String?) {
            noDataFound()
            Log.e("zerStock", s.toString())
        }

        override fun onEverytime() {

        }

    }

    fun noDataFound(){
        view?.rvPatientsComplients?.visibility = View.GONE
        view?.tv_no_data_avaliable?.visibility = View.VISIBLE
    }
}