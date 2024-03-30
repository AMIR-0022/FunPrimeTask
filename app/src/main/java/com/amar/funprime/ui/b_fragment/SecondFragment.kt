package com.amar.funprime.ui.b_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amar.funprime.R
import com.amar.funprime.admob.AdmobInterstitial
import com.amar.funprime.api.AlbumServices
import com.amar.funprime.api.Response
import com.amar.funprime.api.RetrofitHelper
import com.amar.funprime.databinding.FragmentSecondBinding

class SecondFragment : Fragment(), AlbumAdapter.AlbumItemListener {

    private val TAG: String = "Response"

    private lateinit var binding: FragmentSecondBinding

    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var adapter: AlbumAdapter

    // admob interstitial ad
    private lateinit var admobInterstitial: AdmobInterstitial


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_second, container, false)
        binding = FragmentSecondBinding.bind(myView)

        // -->> initialize admobInterstitial to load add
        admobInterstitial = AdmobInterstitial(requireContext(), object : AdmobInterstitial.InsListeners {
            override fun onAdDismiss() {
                admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))
            }
        })
        admobInterstitial.loadInsAd(getString(R.string.interstitial_ad_id))

        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlbumAdapter(requireContext(), this)
        binding.rvMainRecycler.adapter = adapter
        binding.rvMainRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        val albumServices = RetrofitHelper.getInstance().create(AlbumServices::class.java)
        val albumRepository = AlbumRepository(albumServices)
        albumViewModel = ViewModelProvider(this, AlbumViewFactory(albumRepository))[AlbumViewModel::class.java]

        albumViewModel.albumList.observe(requireActivity(), Observer {
            when(it){
                is Response.Loading -> {
                    Log.d(TAG, "Loading.....")
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    it.data?.let { it ->
                        adapter.updateAlbumList(it)

                        binding.rvMainRecycler.adapter = adapter
                        Log.d(TAG, "1. ${it[0].title}")
                        Log.d(TAG, "2. ${it[1].title}")
                        Log.d(TAG, "3. ${it[2].title}")
                        Log.d(TAG, "4. ${it[3].title}")
                        Log.d(TAG, "5. ${it[4].title}")
                    }
                }
                is Response.Error -> {
                    Log.d(TAG, "Something went wrong.....")
                    Toast.makeText(requireContext(), "something went wrong....", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onItemClick(album: Album, position: Int) {

        // -->> show ad if loaded
        if (!admobInterstitial.isInsNull) {
            admobInterstitial.showInsAd()
        }

        // -->> navigate to next fragment with data
        val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(album)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
    }

}