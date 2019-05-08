package ee.traxnet.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ee.traxnet.sample.BuildConfig;
import ee.traxnet.sample.R;
import ee.traxnet.sample.model.ItemList;
import ee.traxnet.sample.type.ListItemType;
import ee.traxnet.sdk.nativeads.TraxnetNativeBannerManager;
import ee.traxnet.sdk.nativeads.TraxnetNativeBannerViewManager;

public class NativeBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_AD = 1;

    private final Context context;
    private final LayoutInflater inflater;

    private List<ItemList> items;

    public NativeBannerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    public void updateItem(List<ItemList> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvTitle;

        ItemHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        void bindView(int position) {
            tvTitle.setText(items.get(position).title);
        }
    }

    public class TraxnetListItemAdHolder extends RecyclerView.ViewHolder {
        private FrameLayout adContainer;
        private TraxnetNativeBannerViewManager nativeBannerViewManager;

        TraxnetListItemAdHolder(View itemView, Context context) {
            super(itemView);
            adContainer = itemView.findViewById(R.id.adContainer);
            nativeBannerViewManager = new TraxnetNativeBannerManager.Builder()
                    .setParentView(adContainer)
                    .setContentViewTemplate(R.layout.traxnet_content_banner_ad_template)
                    .inflateTemplate(context);
        }

        void bindView(int position) {
            TraxnetNativeBannerManager.bindAd(
                    context,
                    nativeBannerViewManager,
                    BuildConfig.traxnetNativeBannerZoneId,
                    items.get(position).id);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_AD) {
            return new TraxnetListItemAdHolder(
                    inflater.inflate(R.layout.list_ad_item, parent, false), context);
        } else {
            return new ItemHolder(inflater.inflate(R.layout.list_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).listItemType == ListItemType.ITEM) {
            return VIEW_TYPE_ITEM;
        }

        return VIEW_TYPE_AD;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            ((ItemHolder) holder).bindView(position);
            return;
        }

        ((TraxnetListItemAdHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
