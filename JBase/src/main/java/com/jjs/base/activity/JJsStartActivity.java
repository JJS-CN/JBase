package com.jjs.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 作者： Jacky
 * 日期：2017-04-19
 * 静态启动页面
 */

public abstract class JJsStartActivity extends RxAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题栏隐藏
        this.getSupportActionBar().hide();
        //设置window显示背景图片,页面销毁后，会自动gc此处消耗的大量内存；
        if (setSplshBackground() != 0) {
            this.getWindow().setBackgroundDrawableResource(setSplshBackground());
        }
        //设置画布可以延伸到屏幕外.
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //判断是否位于栈中，因为如果使用全局异常捕获，会造成启动多个页面，所以需要判断
        if (!isTaskRoot()) {
            finish();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (boolFirst()) {
                    isFirst();
                    getSharedPreferences("app", Context.MODE_PRIVATE).edit().putBoolean("isFirst", false).commit();
                } else {
                    notFirst();
                }
            }
        });
    }

    /**
     * 设置app启动时的window背景显示图片
     * 以background形式显示，所以图片长宽比必须合适
     *
     * @return 传入具体的drawableId
     */
    public abstract int setSplshBackground();

    /**
     * 判断为是第一次启动app，应跳转轮播APP介绍页面
     */
    public abstract void isFirst();

    /**
     * 判断为是第一次启动app，应启动注册页面或main页面
     */
    public abstract void notFirst();

    /**
     * 判断app是否是第一次进入
     */
    private boolean boolFirst() {
        return getSharedPreferences("app", Context.MODE_PRIVATE).getBoolean("isFirst", true);
    }
}
