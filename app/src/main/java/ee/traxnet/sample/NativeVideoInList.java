package ee.traxnet.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAd;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdCompletionListener;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdLoadListener;
import ee.traxnet.sdk.nativeads.TraxnetNativeVideoAdLoader;

public class NativeVideoInList extends AppCompatActivity {

    private final Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_video_in_list);

        RecyclerView rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(NativeVideoInList.this, LinearLayoutManager.VERTICAL, false));
        rvItems.setAdapter(new NativeVideoInList.MyAdapter(NativeVideoInList.this));
    }

    public class TraxnetListItemHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public TraxnetListItemHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bindView(int index) {
            tvTitle.setText("Item " + index);
        }

    }

    public class TraxnetListItemAdHolder extends RecyclerView.ViewHolder {
        FrameLayout adContainer;
        Context mContext;

        public TraxnetListItemAdHolder(View itemView, Context context) {
            super(itemView);
            adContainer = itemView.findViewById(R.id.adContainer);
            mContext = context;
        }

        public void bindView(TraxnetNativeVideoAd ad) {
            adContainer.removeAllViews();
            ad.setCompletionListener(new TraxnetNativeVideoAdCompletionListener() {
                @Override
                public void onAdShowFinished(String adId) {
                    Log.e("Traxnet", "onAdShowFinished: " + adId);
                }
            });
            ad.addToParentView(adContainer);
        }

        public void clear() {
            adContainer.removeAllViews();
        }

    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Context mContext;
        private final LayoutInflater mInflater;

        private static final int VIEW_TYPE_ITEM = 0;
        private static final int VIEW_TYPE_AD = 1;

        private final Map<Integer, TraxnetNativeVideoAd> ads = new HashMap<>();

        public MyAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_AD) {
                return new NativeVideoInList.TraxnetListItemAdHolder(mInflater.inflate(R.layout.list_large_ad_item, parent, false), mContext);
            } else {
                return new NativeVideoInList.TraxnetListItemHolder(mInflater.inflate(R.layout.list_large_item, parent, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            // Just as an example, return 0 or 2 depending on position
            // Note that unlike in ListView adapters, types don't have to be contiguous
            return ((position + 1) % 10) == 0 ? VIEW_TYPE_AD : VIEW_TYPE_ITEM;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == VIEW_TYPE_AD) {
                if (ads.containsKey(position)) {
                    ((NativeVideoInList.TraxnetListItemAdHolder) holder).bindView(ads.get(position));
                } else {
                    ((NativeVideoInList.TraxnetListItemAdHolder) holder).clear();
                    new TraxnetNativeVideoAdLoader.Builder()
                            .setContentViewTemplate(R.layout.traxnet_content_video_ad_template)
                            .setAppInstallationViewTemplate(R.layout.traxnet_app_installation_video_ad_template)
                            .setAutoStartVideoOnScreenEnabled(true)
                            .setFullscreenBtnEnabled(false)
                            .loadAd(NativeVideoInList.this, BuildConfig.traxnetNativeVideoZoneId,
                                    new TraxnetNativeVideoAdLoadListener() {

                                        @Override
                                        public void onNoNetwork() {
                                            Log.e("Traxnet", "No Network Available");
                                        }

                                        @Override
                                        public void onNoAdAvailable() {
                                            Log.e("Traxnet", "No Native Banner Ad Available!");
                                        }

                                        @Override
                                        public void onError(final String error) {
                                            Log.e("Traxnet", "Error: " + error);
                                        }

                                        @Override
                                        public void onRequestFilled(TraxnetNativeVideoAd traxnetNativeVideoAd) {
                                            Log.e("Traxnet", "Native Banner AdView Available");
                                            ads.put(holder.getAdapterPosition(), traxnetNativeVideoAd);
                                            ((NativeVideoInList.TraxnetListItemAdHolder) holder).bindView(traxnetNativeVideoAd);
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(NativeVideoInList.this, "onRequestFilled", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    });
                }
            } else {
                ((NativeVideoInList.TraxnetListItemHolder) holder).bindView(position);
            }

        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

}
