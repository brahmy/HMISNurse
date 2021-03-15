package com.oss.digihealth.nur.ui.login.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.databinding.ActivityTremsAndConditionBinding
import com.oss.digihealth.nur.ui.login.view_model.TermsAndConditionFactory
import com.oss.digihealth.nur.ui.login.view_model.TermsAndConditionViewModel


class TermsAndConditionActivity : AppCompatActivity() {
    private var content: String? = null
    private var binding: ActivityTremsAndConditionBinding? = null
    private var viewModel: TermsAndConditionViewModel? = null
    private var isTermsCondition: Boolean? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }*/

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }*/

    /*override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }*/

    /*@SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (it.window != null)
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }*/
    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_trems_and_condition, container, false)
        viewModel = TermsAndConditionFactory(
            requireActivity().application
        )
            .create(TermsAndConditionViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.okOrder?.setOnClickListener {
            dialog?.dismiss()
        }

        return binding!!.root
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTermsCondition = intent?.getBooleanExtra("isTermsCondition", false)
        if (Utils(this).isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trems_and_condition)

        viewModel = TermsAndConditionFactory(
            application

        ).create(TermsAndConditionViewModel::class.java)
        binding!!.lifecycleOwner = this
        binding?.viewModel = viewModel

        initToolBar()
        val path: String = if (isTermsCondition!!) {
            "file:///android_asset/terms_condition.html"
        }else{
            "file:///android_asset/privacy_policy.html"
        }

        var file =
            "https://docs.google.com/document/d/1lz9SBrToqZjsDcEBaj6z9U9g7abyiAQuJyz8uH7gUqk/edit?"
        val docFile =
            "<iframe src='http://docs.google.com/document/d/1lz9SBrToqZjsDcEBaj6z9U9g7abyiAQuJyz8uH7gUqk/edit&embedded=true'width='100%' height='100%'style='border: none;'></iframe>"
        initWebView(path)

    }

    private fun initToolBar() {
        if (isTermsCondition!!) {
            binding?.tbTermsCondition?.title = getString(R.string.terms_condition_label)
        } else {
            binding?.tbTermsCondition?.title = getString(R.string.privacy_policy)
        }
        binding?.tbTermsCondition?.setNavigationOnClickListener {
            finish()
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(webUrl: String) {
        binding?.webTermsAndConditionView?.settings?.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            allowFileAccess = true
//            setPluginsEnabled(true)
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCachePath(cacheDir.path)
            blockNetworkImage = false
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
        }

        binding?.webTermsAndConditionView?.apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            binding?.webTermsAndConditionView?.webViewClient = MyWebViewClient()
        else
            binding?.webTermsAndConditionView?.webViewClient = MyWebViewClint()
        binding?.webTermsAndConditionView?.settings?.javaScriptEnabled = true
        binding?.webTermsAndConditionView?.loadUrl(webUrl)
    }

    internal inner class MyWebViewClient : WebViewClient() {

        @SuppressLint("NewApi")
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                cookieManager.setAcceptThirdPartyCookies(view, true)
            request!!.url?.toString()?.let { view?.loadUrl(it) }
            return false

        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding?.pbWeb?.visibility = View.GONE
        }
    }

    internal inner class MyWebViewClint : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                cookieManager.setAcceptThirdPartyCookies(view, true)
            view.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding?.pbWeb?.visibility = View.GONE
        }
    }

}
