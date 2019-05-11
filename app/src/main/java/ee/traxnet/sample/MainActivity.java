package ee.traxnet.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ee.traxnet.sample.vast.VASTActivity;
import ee.traxnet.sdk.Traxnet;
import ee.traxnet.sdk.TraxnetAd;
import ee.traxnet.sdk.TraxnetAdRequestListener;
import ee.traxnet.sdk.TraxnetAdRequestOptions;
import ee.traxnet.sdk.TraxnetAdShowListener;
import ee.traxnet.sdk.TraxnetRewardListener;
import ee.traxnet.sdk.TraxnetShowOptions;

public class MainActivity extends AppCompatActivity {

    private Button showAddBtn;
    private TraxnetAd ad;
    private ProgressDialog progressDialog;
    private boolean showCompleteDialog = false;
    private boolean rewarded = false;
    private boolean completed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button requestCachedVideoAdBan = findViewById(R.id.btnRequestVideoAd);
        Button requestStreamVideoAdBtn = findViewById(R.id.btnRequestStreamVideoAd);
        Button requestInterstitialBannerAdBtn = findViewById(R.id.btnRequestInterstitialVideo);
        Button requestBannerAdButton = findViewById(R.id.btnRequestBannerAd);

        progressDialog = new ProgressDialog(this);

        requestCachedVideoAdBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(BuildConfig.traxnetVideoZoneId, TraxnetAdRequestOptions.CACHE_TYPE_CACHED);
            }
        });

        requestStreamVideoAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(BuildConfig.traxnetVideoZoneId, TraxnetAdRequestOptions.CACHE_TYPE_STREAMED);
            }
        });

        requestBannerAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(BuildConfig.traxnetInterstitialBannerZoneId, TraxnetAdRequestOptions.CACHE_TYPE_STREAMED);
            }
        });

        requestInterstitialBannerAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(BuildConfig.traxnetInterstitialVideoZoneId, TraxnetAdRequestOptions.CACHE_TYPE_STREAMED);
            }
        });

        Button vastActivityBtn = findViewById(R.id.btnSecondActivity);

        vastActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VASTActivity.class);
                startActivity(intent);
            }
        });

        showAddBtn = findViewById(R.id.btnShowAd);

        showAddBtn.setEnabled(false);

        showAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ad != null) {
                    showAddBtn.setEnabled(false);
                    TraxnetShowOptions showOptions = new TraxnetShowOptions();
                    ad.show(MainActivity.this, showOptions, new TraxnetAdShowListener() {
                        @Override
                        public void onOpened(TraxnetAd ad) {
                            Log.e("MainActivity", "on ad opened");
                        }

                        @Override
                        public void onClosed(TraxnetAd ad) {
                            Log.e("MainActivity", "on ad closed");
                        }
                    });
                    MainActivity.this.ad = null;
                }
            }
        });

        Button nativeBanner = findViewById(R.id.btnNativeBanner);
        nativeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NativeBannerActivity.class);
                startActivity(intent);
            }
        });

        Button nativeBannerList = findViewById(R.id.btnNativeBannerInList);
        nativeBannerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NativeBannerInList.class);
                startActivity(intent);
            }
        });

        Button nativeVideo = findViewById(R.id.btnNativeVideo);
        nativeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeVideoActivity.class);
                startActivity(intent);
            }
        });

        Button nativeVideoList = findViewById(R.id.btnNativeVideoInList);
        nativeVideoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NativeVideoInList.class);
                startActivity(intent);
            }
        });

        Button nativeWebBanner = findViewById(R.id.btnWebBanner);
        nativeWebBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebBannerActivity.class);
                startActivity(intent);
            }
        });

        Traxnet.setRewardListener(new TraxnetRewardListener() {
            @Override
            public void onAdShowFinished(final TraxnetAd ad, final boolean completed) {
                Log.e("MainActivity", "isCompleted? " + completed +
                        ", adId: " + (ad == null ? "NULL" : ad.getId()) +
                        ", zoneId: " + (ad == null ? "NULL" : ad.getZoneId()));

                MainActivity.this.ad = null;
                showCompleteDialog = true;
                MainActivity.this.completed = completed;
                if (ad != null) {
                    MainActivity.this.rewarded = ad.isRewardedAd();
                }
                if (!completed)
                    Toast.makeText(MainActivity.this,
                            "Ad Closed or No Ad Available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAd(final String zoneId, final int catchType) {

        if (ad == null) {
            TraxnetAdRequestOptions options = new TraxnetAdRequestOptions(catchType);
            showProgressDialog();
            Traxnet.requestAd(MainActivity.this, zoneId, options, new TraxnetAdRequestListener() {
                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this,
                            "ERROR:\n" + error, Toast.LENGTH_SHORT).show();
                    Log.e("Traxnet", "ERROR:" + error);
                    progressDialog.dismiss();
                }

                @Override
                public void onAdAvailable(TraxnetAd ad) {
                    MainActivity.this.ad = ad;
                    showAddBtn.setEnabled(true);
                    Log.e("Traxnet", "adId: " + (ad == null ? "NULL" : ad.getId()) +
                            " available, zoneId: " + (ad == null ? "NULL" : ad.getZoneId()));
                    progressDialog.dismiss();
                }

                @Override
                public void onNoAdAvailable() {
                    Toast.makeText(MainActivity.this, "No Ad Available",
                            Toast.LENGTH_LONG).show();
                    Log.e("Traxnet", "No Ad Available");
                    progressDialog.dismiss();
                }

                @Override
                public void onNoNetwork() {
                    Toast.makeText(MainActivity.this, "No Network",
                            Toast.LENGTH_SHORT).show();
                    Log.e("Traxnet", "No Network Available");
                    progressDialog.dismiss();
                }

                @Override
                public void onExpiring(TraxnetAd ad) {
                    showAddBtn.setEnabled(false);
                    MainActivity.this.ad = null;
                    loadAd(zoneId, catchType);
                }
            });
        }
    }

    private void showProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showCompleteDialog) {
            showCompleteDialog = false;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("View was...")
                    .setMessage("DONE!, completed? " + completed + ", rewarded? " + rewarded)
                    .setNeutralButton("Nothing", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
