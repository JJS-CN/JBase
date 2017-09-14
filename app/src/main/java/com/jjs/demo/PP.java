package com.jjs.demo;

import com.jjs.base.bean.HashBean;
import com.jjs.base.mvp.BasePersenter;

/**
 * 说明：新建P层时，继承base，填入具体view，再编写具体的方法
 * Created by aa on 2017/7/18.
 */

public class PP extends BasePersenter<VV> {
    public PP(VV view) {
        super(view);
    }


    public void cccc(HashBean hashBean) {
        mView.aa();
        //参数取值，进行请求
        hashBean.getValue("key");
        //请求成功后，返回code与参数
        mView.ResponseSuccess(hashBean.getRequestCode(), "");
    }

}
