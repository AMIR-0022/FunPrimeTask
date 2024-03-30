package com.amar.funprime.admob

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kaopiz.kprogresshud.KProgressHUD
import java.util.Timer
import java.util.TimerTask

class AdmobInterstitial(private val context: Context, private val insListeners: InsListeners) {

    private var timer: Timer? = null
    private var progressHUD: KProgressHUD? = null
    private var adID: String? = null
    private var mInterstitialAd: InterstitialAd? = null

    fun loadInsAd(adUnitID: String?) {
        adID = adUnitID
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
            if (mInterstitialAd == null) {
                if (TextUtils.isEmpty(adID)) {
                    adID = "ca-app-pub-3940256099942544/1033173712"
                }
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    context, adID!!,
                    adRequest, object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd
                            Log.i("ins", "onAdLoaded")
                            mInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        Log.d("TAG", "The ad was dismissed.")
                                        insListeners.onAdDismiss()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d("TAG", "The ad failed to show.")
                                        insListeners.onAdDismiss()
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null
                                        Log.d("TAG", "The ad was shown.")
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.i("ins", loadAdError.message)
                            mInterstitialAd = null
                        }
                    })
            }
        }
    }

    fun showInsAd() {
        if (mInterstitialAd != null) {
            progressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Showing Ad")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
            timeCount()
        }
    }

    private fun timeCount() {
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                (context as Activity).runOnUiThread {
                    if (timer != null) {
                        timer!!.cancel()
                        if (progressHUD != null) {
                            if (progressHUD!!.isShowing) {
                                progressHUD!!.dismiss()
                            }
                        }
                        timer = null
                        mInterstitialAd!!.show(context)
                    }
                }
            }
        }, 2000, 1000) // 2000  1000
    }

    interface InsListeners {
        fun onAdDismiss()
    }

    val isInsNull: Boolean
        get() = if (mInterstitialAd != null) false else true
}
