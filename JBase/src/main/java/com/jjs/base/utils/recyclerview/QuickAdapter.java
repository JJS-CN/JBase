package com.jjs.base.utils.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * 使用文档：http://www.jianshu.com/p/b343fcff51b0
 * 说明：
 * Created by aa on 2017/6/26.
 */

public abstract class QuickAdapter<K> extends BaseQuickAdapter<K, QuickHolder> {
    public QuickAdapter(@LayoutRes int layoutResId, @Nullable List<K> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(QuickHolder helper, K data) {
        _convert(helper, data);
    }

    public abstract void _convert(QuickHolder holder, K data);

}
