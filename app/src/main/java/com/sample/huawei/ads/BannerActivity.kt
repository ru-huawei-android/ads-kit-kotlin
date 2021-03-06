/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.sample.huawei.ads

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.R
import com.huawei.hms.ads.banner.BannerView
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : AppCompatActivity(R.layout.activity_banner) {

    private lateinit var bannerView: BannerView
    private lateinit var loadAd: View.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bannerView = BannerView(this)

        loadAd = View.OnClickListener {
            // Убираем существующий bannerView из фрейма под рекламу
            adFrame.removeView(bannerView)
            bannerView.destroy()

            // создаем view для баннера и добавляем его на layout
            bannerView = BannerView(this)
            bannerView.apply {
                adFrame.addView(this);
                adId = getString(R.string.ad_banner)
                bannerAdSize = getBannerSize(sizeRadioGroup.checkedRadioButtonId)
                setBackgroundColor(getColorBackGround(colorRadioGroup.checkedRadioButtonId))
                adListener = adListener
                // загружаем рекламу
                loadAd(AdParam.Builder().build())
            }
        }

        loadAdBtn.setOnClickListener(loadAd)
        loadAdBtn.performClick()
    }

    private fun getBannerSize(id: Int): BannerAdSize {
        return when (id) {
            R.id.size_160_600 -> BannerAdSize.BANNER_SIZE_160_600
            R.id.size_300_250 -> BannerAdSize.BANNER_SIZE_300_250
            R.id.size_320_100 -> BannerAdSize.BANNER_SIZE_320_100
            R.id.size_320_50 -> BannerAdSize.BANNER_SIZE_320_50
            R.id.size_360_57 -> BannerAdSize.BANNER_SIZE_360_57
            R.id.size_360_144 -> BannerAdSize.BANNER_SIZE_360_144
            R.id.size_468_60 -> BannerAdSize.BANNER_SIZE_468_60
            R.id.size_728_90 -> BannerAdSize.BANNER_SIZE_728_90
            R.id.size_smart -> BannerAdSize.BANNER_SIZE_SMART
            R.id.size_dynamic -> BannerAdSize.BANNER_SIZE_DYNAMIC
            else -> BannerAdSize.BANNER_SIZE_320_50
        }
    }

    private fun getColorBackGround(id: Int): Int {
        return when (id) {
            R.id.color_white -> Color.WHITE
            R.id.color_black -> Color.BLACK
            R.id.color_red -> Color.RED
            R.id.color_transparent -> Color.TRANSPARENT
            else -> Color.TRANSPARENT
        }
    }

    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            Log.d(TAG, "onAdLoaded")
        }

        override fun onAdFailed(errorCode: Int) {
            Log.d(TAG,"onAdFailed $errorCode")
            Toast.makeText(applicationContext,
                    Utils.getErrorMessage(errorCode), Toast.LENGTH_LONG).show()
        }

        override fun onAdOpened() {
            Log.d(TAG,"onAdOpened")
        }

        override fun onAdClicked() {
            Log.d(TAG,"onAdClicked")
        }

        override fun onAdLeave() {
            Log.d(TAG,"onAdLeave")
        }

        override fun onAdClosed() {
            Log.d(TAG,"onAdClosed")
        }
    }
}