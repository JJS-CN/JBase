package com.jjs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jjs.base.JJsActivity;
import com.jjs.base.mvp.BaseView;
import com.jjs.base.utils.recyclerview.QuickAdapter;
import com.jjs.base.utils.recyclerview.QuickHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明：
 * Created by aa on 2017/6/29.
 */

public class XXXX extends JJsActivity implements BaseView {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.iv_toTop)
    ImageView ivToTop;
    List<String> strList = new ArrayList<>();
    int moveY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xxx);
        ButterKnife.bind(this);


        for (int i = 0; i < 20; i++) {
            strList.add("Str:" + i);
        }
        QuickAdapter adapter = new QuickAdapter<String>(R.layout.ccc, strList) {
            @Override
            public void _convert(QuickHolder holder, String data) {
                holder.setText(R.id.tv_title, data);
            }
        };
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                moveY += dy;
                Log.e("e", moveY + "");
                ivToTop.setAlpha(moveY / 1000f);
            }
        });
        ivToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivToTop.getAlpha() <= 0.5f) {
                    Toast.makeText(XXXX.this, "当前Alpha值：" + ivToTop.getAlpha() + "，不能回到顶部!", Toast.LENGTH_SHORT).show();
                } else {
                    moveY = 0;
                    rvList.scrollToPosition(0);
                    Toast.makeText(XXXX.this, "回到顶部!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    public void onPermissionFailed(int requestCode, List deniedList) {

    }

    @Override
    public void onPermissionSucceed(int requestCode, List grantList) {

    }


    @OnClick(R.id.iv_toTop)
    public void onViewClicked() {
    }
}
