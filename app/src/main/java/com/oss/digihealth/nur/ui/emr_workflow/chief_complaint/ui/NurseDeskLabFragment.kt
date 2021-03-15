package com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.databinding.FragmentLabBinding
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.ui.NurdeDeskLabChildFragment

class NurseDeskLabFragment : Fragment() {
    private var binding: FragmentLabBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lab,
                container,
                false
            )

        replaceFragment(NurdeDeskLabChildFragment())
        return binding!!.root
    }
    private fun replaceFragment(
        fragment: Fragment
    ) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    fun refreshPage() {
        replaceFragment(NurdeDeskLabChildFragment())
    }

}

