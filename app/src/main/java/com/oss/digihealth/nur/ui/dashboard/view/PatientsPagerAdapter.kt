package com.oss.digihealth.nur.ui.dashboard.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.oss.digihealth.nur.ui.dashboard.model.ChiefComplients
import com.oss.digihealth.nur.ui.dashboard.model.Diagnosis

class PatientsPagerAdapter(
    fm: FragmentManager,
    var diagnosisData: ArrayList<Diagnosis>,
    var chiefComplaintsData: ArrayList<ChiefComplients>
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->
                FragmentOne.newInstance(diagnosisData)

            1 ->  FragmentTwo.newInstance(chiefComplaintsData)
            else -> FragmentThree.newInstance(diagnosisData)
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Top Diagnosis"
            1  -> "Top Complaints"
            else -> "Zero stock"
        }
    }

    fun updateAdapter(diagnosisData: ArrayList<Diagnosis>,
                      chiefComplaintsData: ArrayList<ChiefComplients>){
        this.diagnosisData = diagnosisData
        this.chiefComplaintsData = chiefComplaintsData
    }
}