package ee.traxnet.sample;

import android.app.Application;

import ee.traxnet.sdk.Traxnet;
import ee.traxnet.sdk.TraxnetConfiguration;

public class TraxnetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TraxnetConfiguration config = new TraxnetConfiguration(this);
        config.setDebugMode(true);
        Traxnet.initialize(this, config, BuildConfig.traxnetSampleAppKey);
    }
}
