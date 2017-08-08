package com.jjs.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjs.base.mvp.BasePersenter;

/**
 * 说明：
 * Created by aa on 2017/6/28.
 */

public class BaseActivity extends AppCompatActivity {
    public BasePersenter P;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
