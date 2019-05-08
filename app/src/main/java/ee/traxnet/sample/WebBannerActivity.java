package ee.traxnet.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ee.traxnet.sdk.bannerads.TraxnetBannerType;
import ee.traxnet.sdk.bannerads.TraxnetBannerView;

public class WebBannerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_banner);

        TraxnetBannerView banner1 = findViewById(R.id.banner1);

        banner1.loadAd(WebBannerActivity.this, BuildConfig.traxnetStandardBannerZoneId, TraxnetBannerType.BANNER_320x100);
    }
}
