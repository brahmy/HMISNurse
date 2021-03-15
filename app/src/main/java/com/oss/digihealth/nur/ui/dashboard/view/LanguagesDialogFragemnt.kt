package com.oss.digihealth.nur.ui.dashboard.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogChangeLanguageBinding
import com.oss.digihealth.nur.ui.dashboard.view_model.LanguagesViewModel
import com.oss.digihealth.nur.ui.dashboard.view_model.LanguagesViewModelFactory
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository


class LanguagesDialogFragemnt : DialogFragment() {
    private var content: String? = null
    var binding: DialogChangeLanguageBinding? = null
    private var viewModel: LanguagesViewModel? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var utils: Utils? = null
    companion object{
        var callbacklanguagerefreshProcess: OnLanguageProcessListener? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_change_language, container, false)
        viewModel = LanguagesViewModelFactory(
            requireActivity().application)
            .create(LanguagesViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner=this
        userDetailsRoomRepository = UserDetailsRoomRepository( requireActivity().application!!)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel!!.errorText.observe(
            this.requireActivity(),
            Observer { toastMessage ->
                //utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
                Toast.makeText(activity,toastMessage,Toast.LENGTH_LONG).show()
            })

        val language = appPreferences?.getString(AppConstants?.LANGUAGE)
        if(language?.isNotEmpty()!!)
        {
            if(language.equals("Tamil"))
            {
                binding?.tamilText?.isChecked = true
            }
            else if(language.equals("English"))
            {
                binding?.englishText?.isChecked = true
            }
        }


// Get radio group selected item using on checked change listener
        binding?.radiogroup!!.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = requireView().findViewById(checkedId)
                when (checkedId) {
                    R.id.tamilText -> {
                        radio.isEnabled = true
                    }
                    R.id.englishText -> {
                        radio.isEnabled = true
                    }
                    R.id.teluguText -> {
                        radio.isEnabled = true
                    }
                }

            })
        binding?.saveTextview!!.setOnClickListener {
            val intSelectButton: Int = binding?.radiogroup!!.checkedRadioButtonId
            val radioButton = requireView().findViewById<RadioButton>(intSelectButton)
            Toast.makeText(requireContext(), ""+radioButton.text, Toast.LENGTH_SHORT).show()
            appPreferences?.saveString(AppConstants.LANGUAGE, radioButton.text as String?)
            val dialogListener = activity as LanguagesDialogFragemnt.OnLanguageProcessListener?
            dialogListener!!.onRefreshLanguage()
            dismiss()
        }

        binding?.cancelCardView?.setOnClickListener({
            dialog!!.dismiss()
        })
/*
        // Get radio group selected item using on checked change listener
        binding?.radiogroup!!.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: AppCompatRadioButton = view!!.findViewById(checkedId)
                Toast.makeText(requireContext()," On checked change : ${radio.text}",
                    Toast.LENGTH_SHORT).show()
            })


        // Get radio group selected status and text using button click event
        binding?.saveTextview!!.setOnClickListener{
            // Get the checked radio button id from radio group
            val id: Int = binding?.radiogroup!!.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:AppCompatRadioButton = view!!.findViewById(id)
                Toast.makeText(requireContext(),"On button click : ${radio.text}",
                    Toast.LENGTH_SHORT).show()
            }else{
                // If no radio button checked in this radio group
                Toast.makeText(requireContext(),"On button click : nothing selected",
                    Toast.LENGTH_SHORT).show()
            }
        }*/
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        return binding?.root
    }
    // or a separate test implementation.
    interface OnLanguageProcessListener {
        fun onRefreshLanguage()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { /* this line is main difference for fragment to fragment communication & fragment to activity communication
            fragment to fragment: onInputListener = (OnInputListener) getTargetFragment();
            fragment to activity: onInputListener = (OnInputListener) getActivity();
             */
            callbacklanguagerefreshProcess = targetFragment as OnLanguageProcessListener?

        } catch (e: ClassCastException) {

        }}}