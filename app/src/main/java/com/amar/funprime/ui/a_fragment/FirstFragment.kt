package com.amar.funprime.ui.a_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amar.funprime.R
import com.amar.funprime.admob.AdmobInterstitial
import com.amar.funprime.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    // admob interstitial ad
    private lateinit var admobInterstitial: AdmobInterstitial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_first, container, false)
        binding = FragmentFirstBinding.bind(myView)

        // -->> initialize admobInterstitial to load add
        admobInterstitial = AdmobInterstitial(requireContext(), object : AdmobInterstitial.InsListeners {
            override fun onAdDismiss() {
                admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))
            }
        })
        admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -->> Navigate after a delay of 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({

            // -->> show ad if loaded
            if (!admobInterstitial.isInsNull) {
                admobInterstitial.showInsAd()
            }

            // navigate to next fragment
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }, 5000)

    }

}