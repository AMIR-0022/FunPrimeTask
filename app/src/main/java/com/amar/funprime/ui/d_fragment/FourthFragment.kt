package com.amar.funprime.ui.d_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amar.funprime.R
import com.amar.funprime.admob.AdmobInterstitial
import com.amar.funprime.databinding.FragmentFourthBinding

class FourthFragment : Fragment() {

    private lateinit var binding: FragmentFourthBinding

    // admob interstitial ad
    private lateinit var admobInterstitial: AdmobInterstitial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_fourth, container, false)
        binding = FragmentFourthBinding.bind(myView)


        // -->> initialize admobInterstitial to load add
        admobInterstitial = AdmobInterstitial(requireContext(), object : AdmobInterstitial.InsListeners {
            override fun onAdDismiss() {
                admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))
            }
        })
        admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))



        binding.btnBack.setOnClickListener {
            // -->> show ad if loaded
            if (!admobInterstitial.isInsNull) {
                admobInterstitial.showInsAd()
            }

            // navigate back to recycler fragment
            findNavController().popBackStack()
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }


        return binding.root
    }

}