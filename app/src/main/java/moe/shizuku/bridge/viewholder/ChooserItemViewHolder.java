package moe.shizuku.bridge.viewholder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import moe.shizuku.bridge.BridgeSettings;
import moe.shizuku.bridge.R;
import moe.shizuku.bridge.utils.IntentUtils;
import moe.shizuku.utils.recyclerview.BaseViewHolder;

/**
 * Created by Rikka on 2017/4/6.
 */

public class ChooserItemViewHolder extends BaseViewHolder<ResolveInfo> {

    private static final Object CHECK_SELECT_PAYLOAD = new Object();

    private ImageView icon;
    private ImageView target_badge;
    private TextView title;

    private boolean mEditMode;

    public ChooserItemViewHolder(View itemView, boolean editMode) {
        super(itemView);

        mEditMode = editMode;

        icon = (ImageView) itemView.findViewById(android.R.id.icon);
        target_badge = (ImageView) itemView.findViewById(R.id.target_badge);
        title = (TextView) itemView.findViewById(android.R.id.text1);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode) {
                    BridgeSettings.setActivityForward(getData().activityInfo.name, !BridgeSettings.isActivityForward(getData().activityInfo.name));

                    getAdapter().notifyItemChanged(getAdapterPosition(), CHECK_SELECT_PAYLOAD);
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(getActivity().getIntent().getType());
                    intent.putExtra(Intent.EXTRA_STREAM, getActivity().getIntent().getData());
                    intent.setComponent(ComponentName.createRelative(getData().activityInfo.packageName, getData().activityInfo.name));

                    IntentUtils.startOtherActivity(v.getContext(), intent);

                    getActivity().finish();
                }
            }
        });

        if (!editMode) {
            icon.setStateListAnimator(null);
            title.setStateListAnimator(null);
        }
    }

    @Override
    public void onBind() {
        super.onBind();

        if (getData() == null) {
            icon.setImageDrawable(null);
            title.setText(null);
            return;
        }

        if (mEditMode) {
            checkSelected();
        }

        PackageManager pm = itemView.getContext().getPackageManager();

        icon.setImageDrawable(getData().loadIcon(pm));
        title.setText(getData().loadLabel(pm));
    }

    @Override
    public void onBind(@NonNull List<Object> payloads) {
        super.onBind(payloads);

        for (Object payload: payloads) {
            if (CHECK_SELECT_PAYLOAD.equals(payload)) {
                checkSelected();
            }
        }
    }

    private static WeakReference<ColorMatrixColorFilter> filter = new WeakReference<>(null);

    private void checkSelected() {
        boolean selected = BridgeSettings.isActivityForward(getData().activityInfo.name);
        itemView.setSelected(selected);

        if (!selected) {
            if (filter.get() == null) {
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
                filter = new WeakReference<>(grayColorFilter);
            }

            icon.setColorFilter(filter.get());
        } else {
            icon.setColorFilter(null);
        }
    }
}
