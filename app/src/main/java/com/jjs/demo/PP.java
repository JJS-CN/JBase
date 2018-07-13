package com.jjs.demo;

import android.content.Context;
import android.widget.Toast;

import com.jjs.base.mvp.BasePersenter;

/**
 * 说明：新建P层时，继承base，填入具体view，再编写具体的方法
 * Created by aa on 2017/7/18.
 */

public class PP extends BasePersenter<VV> {
    public PP(VV view) {
        super(view);
    }

    public void show(Context context) {
        Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
    }


}
