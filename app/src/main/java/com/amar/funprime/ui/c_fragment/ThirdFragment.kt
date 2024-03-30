package com.amar.funprime.ui.c_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amar.funprime.R
import com.amar.funprime.databinding.FragmentThirdBinding
import com.amar.funprime.ui.b_fragment.Album
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.util.Random
import android.provider.MediaStore
import com.amar.funprime.admob.AdmobInterstitial
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.ByteArrayOutputStream

class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding

    // admob interstitial ad
    private lateinit var admobInterstitial: AdmobInterstitial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_third, container, false)
        binding = FragmentThirdBinding.bind(myView)

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

        val args: ThirdFragmentArgs by navArgs()
        val receivedAlbum: Album = args.album!!

        binding.title.text = receivedAlbum.title
        Glide.with(requireContext())
            .load(receivedAlbum.thumbnailUrl)
            .into(binding.image)

        Toast.makeText(requireContext(), "successfully received ${receivedAlbum.id}", Toast.LENGTH_SHORT).show()

        binding.btnShare.setOnClickListener {
            shareImageFromUrl(receivedAlbum.thumbnailUrl)
        }
        binding.btnNext.setOnClickListener {
            // -->> show ad if loaded
            if (!admobInterstitial.isInsNull) {
                admobInterstitial.showInsAd()
            }

            // navigate to next fragment
            findNavController().navigate(R.id.action_thirdFragment_to_fourthFragment)
        }

    }

    private fun shareImageFromUrl(imgUrl: String) {
        val requestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)

        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .apply(requestOptions)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "image/jpeg"

                    val outputStream = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                    val imageUri = Uri.parse(MediaStore.Images.Media.insertImage(context?.contentResolver, resource, "Image", null))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    startActivity(Intent.createChooser(shareIntent, "Share Image"))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Not used
                }
            })
    }
}