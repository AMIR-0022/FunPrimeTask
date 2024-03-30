package com.amar.funprime.admob

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.amar.funprime.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

class AdmobBanner : RelativeLayout {
    constructor(context: Context) : super(context) {
        getAttributes(null, 0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getAttributes(attrs, 0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttributes(attrs, defStyleAttr)
        init()
    }

    private var bannerID = "ca-app-pub-3940256099942544/6300978111"

    private fun getAttributes(attributeSet: AttributeSet?, defStyleAttr: Int) {
        if (attributeSet == null) return

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AdmobBanner, defStyleAttr, 0)

        bannerID = typedArray.getString(R.styleable.AdmobBanner_AdMobBannerID) ?: "ca-app-pub-3940256099942544/6300978111"
        typedArray.recycle()
    }

    private fun init() {
        inflate(context, R.layout.admob_banner, this)
        MobileAds.initialize(context, OnInitializationCompleteListener { initializationStatus ->
            initializationStatus.adapterStatusMap.forEach { (adapterClass, status) ->
                Log.d("MyApp", "Adapter name: $adapterClass, Description: ${status.description}, Latency: ${status.latency}")
            }
            loadAd()
        })
    }
    private fun loadAd() {
        val shimmerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimmer)
        val banrContainer = findViewById<RelativeLayout>(R.id.admobBanrContainer)
        val adView = AdView(context)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = bannerID
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("MyApp", "loaded")
                shimmerFrameLayout.visibility = GONE
                banrContainer.removeAllViews()
                banrContainer.addView(adView)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("MyApp", "not$adError")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }

}