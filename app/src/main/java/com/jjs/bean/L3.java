package com.jjs.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public class L3  implements MultiItemEntity {
    String str;

    public L3(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
