package com.jjs.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jjs.R;
import com.jjs.base.widget.BangView;

/**
 * 说明：
 * Created by aa on 2017/9/21.
 */

public class ViewDemoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);
    }

    //爆炸效果
    private void bangTest() {
        TextView tvBang = (TextView) findViewById(R.id.tv_bang);
        new BangView(this).bang(tvBang);
        new BangView(this).bang(tvBang, new BangView.OnBangListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }
        });
        new BangView(this).bang(tvBang, 50, new BangView.OnBangListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }
        });
        //1:初始化传activity对象  2：设置爆炸点颜色  3：设置爆炸点个数 4：设置爆炸 view、爆炸半径、爆炸监听
        new BangView(this).setColors(Color.BLUE, Color.GREEN).setDotNumber(10).bang(tvBang, 50, new BangView.OnBangListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }
}
