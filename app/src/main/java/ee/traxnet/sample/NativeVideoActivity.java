package ee.traxnet.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAd;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdCompletionListener;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdLoadListener;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdLoader;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoIconSet;

public class NativeVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_video);

        new TraxnetNativeVideoAdLoader.Builder()
                .setContentViewTemplate(R.layout.traxnet_content_video_ad_template)
                .setAppInstallationViewTemplate(R.layout.traxnet_app_installation_video_ad_template)
                .setAutoStartVideoOnScreenEnabled(false)
                .setFullscreenBtnEnabled(true)
                .setMuteVideoBtnEnabled(false)
                .setMuteVideo(false)
                .setIconSet(new TraxnetNativeVideoIconSet.Builder()
                        .setFullscreenIcon(R.drawable.full2)
                        .setPlayIcon(R.drawable.play2)
                        .create())
                .loadAd(NativeVideoActivity.this, BuildConfig.traxnetNativeVideoZoneId, new TraxnetNativeVideoAdLoadListener() {

                    @Override
                    public void onNoNetwork() {
                        Log.e("Traxnet", "No Network Available");
                    }

                    @Override
                    public void onNoAdAvailable() {
                        Log.e("Traxnet", "No Native Video Ad Available");
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Traxnet", "Error: " + error);
                    }

                    @Override
                    public void onRequestFilled(TraxnetNativeVideoAd traxnetNativeVideoAd) {
                        traxnetNativeVideoAd.setCompletionListener(new TraxnetNativeVideoAdCompletionListener() {
                            @Override
                            public void onAdShowFinished(String adId) {
                                Log.e("Traxnet", "onAdShowFinished: " + adId);
                            }
                        });
                        traxnetNativeVideoAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("Traxnet", "Native video clicked!");
                            }
                        });
                        traxnetNativeVideoAd.addToParentView((LinearLayout) findViewById(R.id.adParent));
                    }

                });
    }
}
