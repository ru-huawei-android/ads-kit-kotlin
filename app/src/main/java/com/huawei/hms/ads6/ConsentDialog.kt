package com.huawei.hms.ads6

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.huawei.hms.ads.R
import com.huawei.hms.ads.consent.bean.AdProvider
import com.huawei.hms.ads.consent.constant.ConsentStatus
import com.huawei.hms.ads.consent.inter.Consent
import kotlinx.android.synthetic.main.dialog_consent.*

class ConsentDialog(context: Context, providers: MutableList<AdProvider>): Dialog(context,
    R.style.dialog) {

    private var mContext: Context = context
    private var adProviders: List<AdProvider> = providers

    private lateinit var inflater: LayoutInflater
    private lateinit var consentDialogView: View
    private lateinit var initView: View
    private lateinit var moreInfoView: View
    private lateinit var partnersListView: View

    private var callback: ConsentDialogCallback? = null

    interface ConsentDialogCallback {
        fun updateConsentStatus(consentStatus: ConsentStatus?)
    }

    fun setCallback(callback: ConsentDialogCallback?) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogWindow: Window? = window
        dialogWindow?.requestFeature(Window.FEATURE_NO_TITLE)
        inflater = LayoutInflater.from(mContext)
        inflater.apply {
            consentDialogView = inflate(R.layout.dialog_consent, null)
            setContentView(consentDialogView)
            initView = inflate(R.layout.dialog_consent_content, null)
            moreInfoView = inflate(R.layout.dialog_consent_moreinfo, null)
            partnersListView = inflate(R.layout.dialog_consent_partner_list, null)
        }

        showInitConsentInfo()
    }

    // сохраняем выбор пользвоателя в shared preferences
    private fun updateConsentStatus(consentStatus: ConsentStatus) {
        Consent.getInstance(mContext).setConsentStatus(consentStatus)

        mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(SP_CONSENT_KEY, consentStatus.value)
                .apply()

        callback?.updateConsentStatus(consentStatus)
    }

    private fun showInitConsentInfo() {
        addContentView(initView)
        addInitButtonAndLinkClick(consentDialogView)
    }


    private fun addInitButtonAndLinkClick(rootView: View) {

        // Навешиваем обработчики на кнопки согласия и отказа
        rootView.findViewById<Button>(R.id.btn_consent_init_yes)?.setOnClickListener {
            dismiss()
            updateConsentStatus(ConsentStatus.PERSONALIZED)
        }

        rootView.findViewById<Button>(R.id.btn_consent_init_skip)?.setOnClickListener {
            dismiss()
            updateConsentStatus(ConsentStatus.NON_PERSONALIZED)
        }

        val spanInitText = SpannableStringBuilder(mContext.getString(R.string.consent_init_text))

        val initTouchHere: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTouchHereInfo()
            }
        }
        val colorSpan = ForegroundColorSpan(Color.parseColor("#0000FF"))
        val initTouchHereStart = mContext.resources.getInteger(R.integer.init_here_start)
        val initTouchHereEnd = mContext.resources.getInteger(R.integer.init_here_end)

        spanInitText.setSpan(initTouchHere, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanInitText.setSpan(colorSpan, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        rootView.findViewById<TextView>(R.id.consent_center_init_content).apply {
            this?.text = spanInitText
            this?.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun showTouchHereInfo() {
        addContentView(moreInfoView)
        addMoreInfoButtonAndLinkClick(consentDialogView)
    }

    private fun addMoreInfoButtonAndLinkClick(rootView: View) {
        rootView.findViewById<Button>(R.id.btn_consent_more_info_back)?.setOnClickListener { showInitConsentInfo() }

        val spanMoreInfoText = SpannableStringBuilder(mContext.getString(R.string.consent_more_info_text))
        val moreInfoTouchHere: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPartnersListInfo()
            }
        }

        val moreInfoTouchHereStart = mContext.resources.getInteger(R.integer.more_info_here_start)
        val moreInfoTouchHereEnd = mContext.resources.getInteger(R.integer.more_info_here_end)

        spanMoreInfoText.setSpan(moreInfoTouchHere, moreInfoTouchHereStart, moreInfoTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanMoreInfoText.setSpan(ForegroundColorSpan(Color.parseColor("#0000FF")), moreInfoTouchHereStart, moreInfoTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

       rootView.findViewById<TextView>(R.id.consent_center_more_info_content)?.apply {
           text = spanMoreInfoText
           movementMethod = LinkMovementMethod.getInstance()
       }
    }

    private fun showPartnersListInfo() {

        partnersListView.findViewById<TextView>(R.id.partners_list_content)?.apply {
            text = ""
            for (learnAdProvider in adProviders) {
                val link = ("<font color='#0000FF'><a href=" + learnAdProvider.privacyPolicyUrl + ">"
                        + learnAdProvider.name + "</a> &nbsp;")
                this.append(Html.fromHtml(link))
            }
            movementMethod = LinkMovementMethod.getInstance()
        }

        addContentView(partnersListView)
        addPartnersListButtonAndLinkClick(consentDialogView)
    }

    private fun addPartnersListButtonAndLinkClick(rootView: View) {
        rootView.findViewById<Button>(R.id.btn_partners_list_back)?.setOnClickListener { showTouchHereInfo() }
    }

    private fun addContentView(view: View) {
        consent_center_layout.removeAllViews()
        consent_center_layout.addView(view)
    }
}