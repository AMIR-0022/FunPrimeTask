package com.amar.funprime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amar.funprime.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --->>> admob for banner
    private lateinit var consentInformation: ConsentInformation
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        /** IMPORTANT NOTE */
        /** this is new way to show ads but currently not using this to show ads
         * I'm using custom view to show ads with shimmer effect
         */
        // requestConsentInfoUpdate()

    }


    private fun requestConsentInfoUpdate() {

        val debugSettings = ConsentDebugSettings.Builder(this)
            .addTestDeviceHashedId("33BE2250B43518CCDA7DE426D04EE231")
            .build()

        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()

//        val params = ConsentRequestParameters
//            .Builder()
//            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(this, params, {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                this
            ) { loadAndShowError ->
                if (loadAndShowError!=null){
                    Snackbar.make(binding.adView, loadAndShowError.message, Snackbar.LENGTH_INDEFINITE).apply {
                        setAction("Reload"){ requestConsentInfoUpdate() }
                            .setActionTextColor(ContextCompat.getColor(context, R.color.black))
                            .show()
                    }
                } else {
                    isMobileAdsInitializeCalled.getAndSet(true)
                    loadAds()
                }
            }
        }, { requestConsentError ->
            Toast.makeText(this, requestConsentError.message, Toast.LENGTH_SHORT).show()
        })

    }

    private fun loadAds() {
        if(consentInformation.canRequestAds()) {
            MobileAds.initialize(this) {
                bannerAdsCallBack()
            }
            val adRequest: AdRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }

    private fun bannerAdsCallBack() {
        binding.adView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }

}