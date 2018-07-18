package com.jjs.base.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * 使用文档：http://www.jianshu.com/p/b343fcff51b0
 * Created by aa on 2017/6/26.
 */

public abstract class QuickAdapter<K> extends BaseQuickAdapter<K, QuickHolder> {
    public QuickAdapter(@LayoutRes int layoutResId, @Nullable List<K> data) {
        super(layoutResId, data);
    }

    public QuickAdapter() {
        super(null);
        initMultiType();
    }

    /**
     * 多布局时，重写此方法来返回判断type和对应布局
     */
    public void initMultiType() {
        //you can init the MultiType
        //根据实体类中的类型，返回此类型用来判断type
     /*   setMultiTypeDelegate(new MultiTypeDelegate<Entity>() {
            @Override
            protected int getItemType(Entity entity) {
                //根据你的实体类来判断布局类型
                return entity.type;
            }
        });
        //根据返回的type，加载不同的布局
        getMultiTypeDelegate()
                .registerItemType(Entity.TEXT, R.layout.item_text_view)
                .registerItemType(Entity.IMG, R.layout.item_image_view);*/
        //convert{}中，根据type加载不同的数据
      /*  switch (helper.getItemViewType()){
            case Entity.TEXT:
                break;
            case Entity.IMG:
                break;
        }*/
    }


    @Override
    protected void convert(QuickHolder helper, K data) {
        _convert(helper, data);
    }

    public abstract void _convert(QuickHolder holder, K data);

}
