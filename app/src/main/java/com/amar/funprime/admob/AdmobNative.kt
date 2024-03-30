package com.amar.funprime.admob

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.amar.funprime.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.kaopiz.kprogresshud.KProgressHUD


class AdmobNative : RelativeLayout {
    private var progressHUD: KProgressHUD? = null
    private var adLoaded = false

    constructor(context: Context?) : super(context) {
        getAtter(null, 0)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        getAtter(attrs, 0)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        getAtter(attrs, defStyleAttr)
        init()
    }

    private var nativeID: String? = "ca-app-pub-3940256099942544/2247696110"
    private var showMediumLayout = true
    private fun getAtter(attributeSet: AttributeSet?, defStyleAttr: Int) {
        if (attributeSet == null) return
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.AdmobNative, defStyleAttr, 0)
        nativeID = typedArray.getString(R.styleable.AdmobNative_nativeID)
        showMediumLayout = typedArray.getBoolean(R.styleable.AdmobNative_showMedimAd, true)
        if (TextUtils.isEmpty(nativeID)) nativeID = "ca-app-pub-3940256099942544/2247696110"
    }

    private fun init() {
        MobileAds.initialize(
            context
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }

            // Start loading ads here...
        }
        if (showMediumLayout) inflate(context, R.layout.admob_native_medium, this) else inflate(
            context, R.layout.admob_native_small, this
        )
        val shimmerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimmer)
        val templateView = findViewById<TemplateView>(R.id.myNativeTemplate)
        val adLoader = AdLoader.Builder(context, nativeID!!)
            .forNativeAd { nativeAd ->
                val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(
                    ColorDrawable(
                        context.resources.getColor(R.color.white)
                    )
                ).build()
                shimmerFrameLayout.hideShimmer()
                templateView.visibility = VISIBLE
                templateView.setStyles(styles)
                templateView.setNativeAd(nativeAd)
                adLoaded = true // Set the flag to indicate that the ad is loaded
                hideProgressDialog() // Hide the progress dialog when the ad is loaded
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d("haider", loadAdError.toString())
                    hideProgressDialog()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())

        // Show the progress dialog
        showProgressDialog()
    }

    private fun showProgressDialog() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading Ad")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        progressHUD!!.show()
    }

    private fun hideProgressDialog() {
        if (progressHUD != null && adLoaded) {
            progressHUD!!.dismiss()
        }
    }
}
