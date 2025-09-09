package com.example.lecture_firebase.AdDemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lecture_firebase.AdDemo.ui.theme.Lecture_firebaseTheme
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class BannerAdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lecture_firebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    BannerScreen(Modifier.padding(innerPadding))

                }
            }
        }
    }


    val TAG = "====="

    @Composable
    fun BannerScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        val adView = remember { AdView(context) }

        // Setup and load the adview.
        // Set the unique ID for this specific ad unit.
        adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"

        val adSize =
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(LocalContext.current, 360)
        adView.setAdSize(adSize)

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

}
