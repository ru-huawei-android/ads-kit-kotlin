package com.sample.huawei.ads

import android.app.Application
import com.huawei.hms.ads.HwAds

class AdsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        HwAds.init(this)
    }

}