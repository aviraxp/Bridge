package moe.shizuku.bridge.adapter;

import android.content.pm.ResolveInfo;
import android.view.View;

import java.util.List;

import moe.shizuku.bridge.R;
import moe.shizuku.bridge.viewholder.ChooserItemViewHolder;
import moe.shizuku.utils.recyclerview.BaseRecyclerViewAdapter;
import moe.shizuku.utils.recyclerview.BaseViewHolder;

/**
 * Created by Rikka on 2017/4/6.
 */

public class ChooserAdapter extends BaseRecyclerViewAdapter<ResolveInfo> {

    private boolean mEditMode;

    public ChooserAdapter(List<ResolveInfo> resolveInfo, boolean editMode) {
        super();

        mEditMode = editMode;

        addItems(R.layout.resolve_grid_item, resolveInfo);
    }

    @Override
    public BaseViewHolder<ResolveInfo> onCreateViewHolder(View view, int viewType) {
        return new ChooserItemViewHolder(view, mEditMode);
    }
}
