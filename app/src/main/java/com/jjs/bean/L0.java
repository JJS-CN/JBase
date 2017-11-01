package com.jjs.bean;

import com.jjs.base.bean.ExpandableItem;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public class L0 extends ExpandableItem<L1> {
    public L0(String str) {
        this.L0str = str;
    }

    String L0str;

    public String getL0str() {
        return L0str;
    }

    public void setL0str(String l0str) {
        L0str = l0str;
    }

    @Override
    protected int _getLevel() {
        return 0;
    }

    @Override
    protected int _getItemType() {
        return 0;
    }
}
