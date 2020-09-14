package com.huawei.hms.ads6

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.R
import com.huawei.hms.ads.reward.Reward
import com.huawei.hms.ads.reward.RewardAd
import com.huawei.hms.ads.reward.RewardAdLoadListener
import com.huawei.hms.ads.reward.RewardAdStatusListener
import kotlinx.android.synthetic.main.activity_reward.*
import kotlinx.android.synthetic.main.text_view.*

class RewardActivity : AppCompatActivity(R.layout.activity_reward) {

    private lateinit var rewardedAd: RewardAd

    private var isRewarded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRewardAd()
        getRewardBtn.setOnClickListener {
            rewardAdShow()
        }
    }

    private fun giveReward(giveReward: Boolean) {

        if (giveReward) {
            rewardResult.text = getString(R.string.reward_given)
            rewardResult.setTextColor(resources.getColor(R.color.colorRewardGiven))
        } else {
            rewardResult.text = getString(R.string.reward_not_given)
            rewardResult.setTextColor(resources.getColor(R.color.colorRewardRejected))
        }
    }

    private fun rewardAdShow() {
        if (rewardedAd.isLoaded) {
            isRewarded = false
            rewardedAd.show(this, rewardAdStatusListener)
        }
    }

    private fun loadRewardAd() {
        rewardedAd = RewardAd(this, getString(R.string.ad_rewarded_vertical)) //TODO: orientation check
        rewardedAd.loadAd(AdParam.Builder().build(), rewardAdLoadListener)
    }

    private val rewardAdStatusListener = object : RewardAdStatusListener() {
        override fun onRewardAdClosed() {
            Toast.makeText(applicationContext, "onRewardAdClosed", Toast.LENGTH_SHORT).show()
            giveReward(isRewarded)
            loadRewardAd()
        }

        override fun onRewardAdFailedToShow(errorCode: Int) {
            Toast.makeText(applicationContext,
                Utils.getErrorMessage(errorCode), Toast.LENGTH_SHORT).show()
        }

        override fun onRewardAdOpened() {
            Toast.makeText(applicationContext, "onRewardAdOpened", Toast.LENGTH_SHORT).show()
        }

        override fun onRewarded(reward: Reward) {
            Toast.makeText(applicationContext, "Ad finished, you get a reward", Toast.LENGTH_SHORT).show()
            isRewarded = true
            loadRewardAd()
        }

    }

    private val rewardAdLoadListener = object : RewardAdLoadListener() {
        override fun onRewardAdFailedToLoad(errorCode: Int) {
            Toast.makeText(applicationContext,
                Utils.getErrorMessage(errorCode), Toast.LENGTH_SHORT).show()
        }

        override fun onRewardedLoaded() {
        }
    }
}