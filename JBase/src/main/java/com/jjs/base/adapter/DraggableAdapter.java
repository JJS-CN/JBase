package com.jjs.base.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;

import java.util.List;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public abstract class DraggableAdapter<T> extends BaseItemDraggableAdapter<T, QuickHolder> {
    public DraggableAdapter(@LayoutRes int layoutId, List<T> data) {
        super(layoutId,data);
    }

    @Override
    protected void convert(QuickHolder helper, T data) {
        _convert(helper, data);
    }

    public abstract void _convert(QuickHolder holder, T data);
}
