package com.jjs.base.a_demo;

import com.jjs.base.mvp.BasePersenter;

/**
 * 说明：新建P层时，继承base，填入具体view，再编写具体的方法
 * Created by aa on 2017/7/18.
 */

public class PP extends BasePersenter<VV>{
    public PP(VV view) {
        super(view);
    }

    public void cccc(){
        mView.aa();
    }
}
