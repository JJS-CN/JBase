package com.jjs.base.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jjs.base.BaseStore;
import com.jjs.base.utils.GlideUtils;

/**
 * 说明：
 * Created by aa on 2017/6/26.
 */

public class QuickHolder extends BaseViewHolder {

    public QuickHolder(View view) {
        super(view);
    }

    @Override
    public QuickHolder setGone(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.GONE : View.VISIBLE);
        return this;
    }

    public QuickHolder setVisibility(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public QuickHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
        if (!url.startsWith("http")) {
            url = BaseStore.BaseImageUrl + url;
        }
        GlideUtils.load(view.getContext(), url, view).asSimple();
        return this;
    }
}
