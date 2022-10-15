package com.example.deepstylo


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.deepstylo.Utils.InternetUtil.isOnline
import com.example.deepstylo.adapters.AutoSliderAdapter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity() {

    lateinit var sliderAdapter: AutoSliderAdapter
    private var mInterstitialAd: InterstitialAd? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MobileAds.initialize(this)
        loadAd()

        val images = ArrayList<Int>()

        images.add(R.drawable.b)
        images.add(R.drawable.d)
        images.add(R.drawable.i)
        images.add(R.drawable.a)
        images.add(R.drawable.e)
        images.add(R.drawable.h)


        sliderAdapter = AutoSliderAdapter(images)
        imageSlider.setSliderAdapter(sliderAdapter)
        imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        imageSlider.startAutoCycle()



        insertImageBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (isOnline()) {
                    ImagePicker.with(this@HomeActivity)
                        .galleryOnly()
                        .crop()
                        .start()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@HomeActivity,
                            "No Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }



            cameraBtn.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (isOnline()) {
                        ImagePicker.with(this@HomeActivity)
                            .cameraOnly()
                            .crop()
                            .start()
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@HomeActivity,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun loadAd() {

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {

            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            showAd(data!!.data!!)


        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(
                this@HomeActivity,
                ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(this@HomeActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()

        }


    }

    fun showAd(uri : Uri){

        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        intent.putExtra("image",uri)

        if (mInterstitialAd != null){
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdDismissedFullScreenContent() {
                    startActivity(intent)
                    finish()

                }


            }
            mInterstitialAd?.show(this)

        } else {
            startActivity(intent)
            finish()
        }
    }
}
