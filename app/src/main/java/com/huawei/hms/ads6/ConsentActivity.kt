package com.huawei.hms.ads6

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.R
import com.huawei.hms.ads.consent.bean.AdProvider
import com.huawei.hms.ads.consent.constant.ConsentStatus
import com.huawei.hms.ads.consent.constant.DebugNeedConsent
import com.huawei.hms.ads.consent.inter.Consent
import com.huawei.hms.ads.consent.inter.ConsentUpdateListener

class ConsentActivity : AppCompatActivity(R.layout.activity_consent),
    ConsentDialog.ConsentDialogCallback {

    private val TAG = ConsentActivity::class.java.simpleName

    private var providers: MutableList<AdProvider> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkConsentStatus()
    }

    private fun checkConsentStatus() {
        Consent.getInstance(this).apply {
            setConsentStatus(ConsentStatus.UNKNOWN)
            addTestDeviceId(testDeviceId)
            setDebugNeedConsent(DebugNeedConsent.DEBUG_NEED_CONSENT)
            requestConsentUpdate(consentUpdateListener)
        }
    }

    private val consentUpdateListener = object : ConsentUpdateListener {
        override fun onSuccess(consentStatus: ConsentStatus, isNeedConsent: Boolean, adProviders: List<AdProvider>) {
            Log.d(TAG, "ConsentStatus: $consentStatus, isNeedConsent: $isNeedConsent")

            if (isNeedConsent) {
                // If ConsentStatus is set to UNKNOWN, re-collect user consent.
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    providers.clear()
                    providers.addAll(adProviders)
                    showConsentDialog()
                } else {
                    // If ConsentStatus is set to PERSONALIZED or NON_PERSONALIZED, no dialog box is displayed to collect user consent.
                }
            } else {
                // If a country does not require your app to collect user consent before displaying ads, your app can request a personalized ad directly.
                Log.d(TAG,"User is NOT need Consent")
            }
        }

        // In this demo,if the request fails ,you can load a non-personalized ad by default.
        override fun onFail(errorDescription: String) {
            Log.d(TAG, "User's consent status failed to update: $errorDescription")
            Toast.makeText(applicationContext, "User's consent status failed to update: $errorDescription", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConsentDialog() {
        // Start to process the consent dialog box.
        ConsentDialog(this, providers).apply {
            setCallback(this@ConsentActivity)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    override fun updateConsentStatus(consentStatus: ConsentStatus?) {
        // Загружаем рекламу в зависимости от согласия
    }
}

