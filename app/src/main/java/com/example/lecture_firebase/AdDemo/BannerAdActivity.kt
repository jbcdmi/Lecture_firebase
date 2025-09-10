package com.example.lecture_firebase.AdDemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lecture_firebase.AdDemo.ui.theme.Lecture_firebaseTheme
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class BannerAdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loadInterstitial()
        setContent {
            Lecture_firebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    BannerScreen(Modifier.padding(innerPadding))

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Button(onClick = {
                            showLoadedInterstitial()
                        }) {
                            Text("Show Interstitial Ad")
                        }
                    }

                }
            }
        }
    }

    val TAG = "====="

    @Composable
    fun BannerScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        val adView = remember { AdView(context).apply {
            // Setup and load the adview.
            // Set the unique ID for this specific ad unit.
            this.adUnitId = "ca-app-pub-3940256099942544/9214589741"

            val adSize =
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this@BannerAdActivity, 360)
            this.setAdSize(adSize)
        } }

        if(adView.isShown) return

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            AndroidView(modifier = modifier.fillMaxWidth(), factory = {
                adView
            })
        }

        // Set an AdListener to receive callbacks for various ad events.
        adView.adListener =
            object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "Banner ad was loaded.")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Banner ad failed to load: ${error.message}")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Banner ad recorded an impression.")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "Banner ad was clicked.")
                }
            }

        // Create an AdRequest and load the ad.
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        DisposableEffect(Unit) {
            // Destroy the AdView to prevent memory leaks when the screen is disposed.
            onDispose { adView.destroy() }
        }

    }


    var interstitialAd: InterstitialAd? = null
    fun loadInterstitial() {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                }
            },
        )
    }

    fun showLoadedInterstitial() {
        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null

                    loadInterstitial()
                    // start intent to next activity
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    // Called when ad is clicked.
                    Log.d(TAG, "Ad was clicked.")
                }
            }
        interstitialAd?.show(this)
    }


}
