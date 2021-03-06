package com.sample.huawei.ads

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.ads.R
import kotlinx.android.synthetic.main.activity_interstitial.*

class InterstitialActivity : AppCompatActivity(R.layout.activity_interstitial) {

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var loadAd: View.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadAd = View.OnClickListener {
            interstitialAd = InterstitialAd(this)
            interstitialAd.adId = getAdId(interstitialRadioGroup.checkedRadioButtonId)
            interstitialAd.adListener = adListener
            interstitialAd.loadAd(AdParam.Builder().build())
        }
        loadInterstitialAdBtn.setOnClickListener(loadAd)
    }

    private fun getAdId(id: Int): String? {
        return when (id) {
            R.id.display_image -> getString(R.string.ad_interstitial_img)
            else -> getString(R.string.ad_interstitial_vid)
        }
    }

    private fun showInterstitial() {
        when {
            // показываем рекламу только если она загрузилась
            interstitialAd.isLoaded -> interstitialAd.show()
            else -> Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
        }
    }


    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            Log.d(TAG, "onAdLoaded")
            showInterstitial()
        }

        override fun onAdFailed(errorCode: Int) {
            Log.d(TAG, "onAdFailed $errorCode")
            Toast.makeText(applicationContext,
                    Utils.getErrorMessage(errorCode), Toast.LENGTH_LONG).show()
        }

        override fun onAdClosed() {
            Log.d(TAG, "onAdClosed")
        }
    }

}