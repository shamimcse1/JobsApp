package com.example.util;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ixidev.gdpr.GDPRChecker;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

public class PopUpAds {
    public static <T> void showInterstitialAds(Activity activity, int adapterPosition, T item, RvOnClickListener<T> clickListener) {
        if (AppUtil.isInterstitial) {
            AppUtil.adCountIncrement += 1;
            if (AppUtil.adCountIncrement == AppUtil.interstitialAdCount) {
                switch (AppUtil.adNetworkType) {
                    case AppUtil.admobAd:
                        AppUtil.adCountIncrement = 0;
                        GDPRChecker.Request request = GDPRChecker.getRequest();
                        AdRequest.Builder builder = new AdRequest.Builder();
                        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");
                            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                        }
                        InterstitialAd.load(activity, AppUtil.interstitialId, builder.build(), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                super.onAdLoaded(interstitialAd);
                                interstitialAd.show(activity);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        clickListener.onItemClick(item, adapterPosition);
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        clickListener.onItemClick(item, adapterPosition);
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                clickListener.onItemClick(item, adapterPosition);
                            }
                        });
                        break;
                    case AppUtil.startAppAd:
                        AppUtil.adCountIncrement = 0;
                        StartAppAd startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                                startAppAd.showAd(new AdDisplayListener() {
                                    @Override
                                    public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
                                        clickListener.onItemClick(item, adapterPosition);
                                    }

                                    @Override
                                    public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {

                                    }

                                    @Override
                                    public void adClicked(com.startapp.sdk.adsbase.Ad ad) {

                                    }

                                    @Override
                                    public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {
                                        clickListener.onItemClick(item, adapterPosition);
                                    }
                                });
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                                clickListener.onItemClick(item, adapterPosition);
                            }
                        });
                        break;
                    case AppUtil.facebookAd:
                        AppUtil.adCountIncrement = 0;
                        com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(activity, AppUtil.interstitialId);
                        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                interstitialAd.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        };
                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).withCacheFlags(CacheFlag.ALL).build();
                        interstitialAd.loadAd(loadAdConfig);
                        break;
                    case AppUtil.appLovinMaxAd:
                        AppUtil.adCountIncrement = 0;
                        MaxInterstitialAd maxInterstitialAd = new MaxInterstitialAd(AppUtil.interstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                maxInterstitialAd.showAd();
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {
                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                clickListener.onItemClick(item, adapterPosition);
                            }
                        });
                        maxInterstitialAd.loadAd();
                        break;
                    case AppUtil.wortiseAd:
                        AppUtil.adCountIncrement = 0;
                        com.wortise.ads.interstitial.InterstitialAd mInterstitial = new com.wortise.ads.interstitial.InterstitialAd(activity, AppUtil.interstitialId);
                        mInterstitial.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                clickListener.onItemClick(item, adapterPosition);
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                if (mInterstitial.isAvailable()) {
                                    mInterstitial.showAd();
                                }
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }
                        });
                        mInterstitial.loadAd();
                        break;
                }
            } else {
                clickListener.onItemClick(item, adapterPosition);
            }
        } else {
            clickListener.onItemClick(item, adapterPosition);
        }
    }
}
