package com.jjs.base.utils.recyclerview;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public abstract class ExpandableAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, QuickHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableAdapter(List<MultiItemEntity> data) {
        super(data);
        initItemTypes();
    }
    public abstract void initItemTypes();

    @Override
    protected void convert(QuickHolder helper, MultiItemEntity item) {
        _convert(helper, item);
    }

    public abstract void _convert(QuickHolder holder, MultiItemEntity data);
}
