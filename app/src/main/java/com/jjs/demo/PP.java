package com.jjs.demo;

import android.content.Context;
import android.widget.Toast;

import com.jjs.base.mvp.BasePersenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * 说明：新建P层时，继承base，填入具体view，再编写具体的方法
 * Created by aa on 2017/7/18.
 */

public class PP extends BasePersenter<VV> {

    public PP(RxAppCompatActivity rxActivity, VV view) {
        super(rxActivity, view);
    }

    public PP(RxFragment rxFragment, VV view) {
        super(rxFragment, view);
    }

    public PP(RxDialogFragment rxDialogFragment, VV view) {
        super(rxDialogFragment, view);
    }

    public void show(Context context) {
        Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
    }


}
