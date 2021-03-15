package com.oss.digihealth.nur.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.ui.dashboard.model.Diagnosis
import kotlinx.android.synthetic.main.fragment_patients_complients.view.*

class FragmentOne() : Fragment(){

    private var diagnosisAdapter: DiagnosisAdapter? = null

    val data: ArrayList<Diagnosis>?
        get() = if (arguments == null) null else requireArguments().getSerializable(ARG_NAME) as ArrayList<Diagnosis>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_patients_complients, container, false)
        diagnosisAdapter = DiagnosisAdapter(requireContext(),data)
        view.rvPatientsComplients.layoutManager = LinearLayoutManager(requireActivity())
        view.rvPatientsComplients.adapter = diagnosisAdapter
        return  view

    }


    companion object {
        const val ARG_NAME = "name"


        fun newInstance(data: ArrayList<Diagnosis>): FragmentOne {
            val fragment = FragmentOne()

            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, data)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

}