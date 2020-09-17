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
                // Если ConsentStatus == UNKNOWN, заново получаем согласие пользователя.
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    providers.clear()
                    providers.addAll(adProviders)
                    showConsentDialog()
                } else {
                    // Если ConsentStatus == PERSONALIZED или NON_PERSONALIZED, никакого диалога показывать не надо
                }
            } else {
                // Если в регионе не требуется пользовательское согласие на персонализированную рекламу
                // Можно запросить её явно
                Log.d(TAG,"User consent not needed")
            }
        }

        override fun onFail(errorDescription: String) {
            Log.d(TAG, "User's consent status failed to update: $errorDescription")
            Toast.makeText(applicationContext, getString(R.string.consent_update_failed, errorDescription), Toast.LENGTH_LONG).show()
        }
    }

    private fun showConsentDialog() {
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

