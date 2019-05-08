package ee.traxnet.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import ee.traxnet.sdk.AdRequestCallback;
import ee.traxnet.sdk.nativeads.TraxnetNativeBannerManager;
import ee.traxnet.sdk.nativeads.TraxnetNativeBannerViewManager;

public class NativeBannerActivity extends AppCompatActivity {
    private FrameLayout adContainer;
    private TraxnetNativeBannerViewManager nativeBannerViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner);

        adContainer = findViewById(R.id.adContainer);
        initTraxnetNative();
        getTraxnetAd();
    }

    private void initTraxnetNative() {
        nativeBannerViewManager = new TraxnetNativeBannerManager.Builder()
                .setParentView(adContainer)
                .setContentViewTemplate(R.layout.traxnet_content_banner_ad_template)
                .setAppInstallationViewTemplate(R.layout.traxnet_app_installation_banner_ad_template)
                .inflateTemplate(this);
    }

    private void getTraxnetAd() {
        TraxnetNativeBannerManager.getAd(this, BuildConfig.traxnetNativeBannerZoneId,
                new AdRequestCallback() {
                    @Override
                    public void onResponse(String[] adId) {
                        onAdResponse(adId);
                    }

                    @Override
                    public void onFailed(String message) {
                        Log.e("getTraxnetAd onFailed", message);
                    }
                });
    }

    private void onAdResponse(final String[] adId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TraxnetNativeBannerManager.bindAd(
                        NativeBannerActivity.this,
                        nativeBannerViewManager,
                        BuildConfig.traxnetNativeBannerZoneId,
                        adId[0]);
            }
        });
    }
}
