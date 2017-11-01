package com.jjs.base.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 说明：实现多级菜单切换，需要实现如下
 * 数据实体类需要继承此基类，具体数据筛选整理请参考demo
 * Created by aa on 2017/10/30.
 */

public abstract class ExpandableItem<T> extends AbstractExpandableItem<T> implements MultiItemEntity {
    //返回层次，从0开始，如果不在乎请返回负数
    @Override
    public int getLevel() {
        return _getLevel();
    }

    //返回itemType，用于convant中绑定不同数据使用
    @Override
    public int getItemType() {
        return _getItemType();
    }

    protected abstract int _getLevel();

    protected abstract int _getItemType();
}
