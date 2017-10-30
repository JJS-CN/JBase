package com.jjs.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public class L1 extends AbstractExpandableItem<L3>implements MultiItemEntity {
    @Override
    public int getLevel() {
        return 1;
    }

    public L1(String str) {
        this.L1str = str;
    }

    String L1str;

    public String getL1str() {
        return L1str;
    }

    public void setL1str(String l1str) {
        L1str = l1str;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
