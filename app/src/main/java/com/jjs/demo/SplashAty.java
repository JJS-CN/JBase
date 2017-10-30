/*
package com.jjs.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jjs.MainActivity;
import com.jjs.R;
import com.jjs.base.utils.recyclerview.QuickAdapter;
import com.jjs.base.utils.recyclerview.QuickHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

*/
/**
 * 说明：
 * Created by aa on 2017/6/16.
 *//*


public class SplashAty extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_add)
    Button btnAdd;


    List<String> list;
    QuickAdapter adapter_quick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        list = new ArrayList<>();
        */
/** 1：数据类型、2：item布局、3：数据源 *//*

        adapter_quick = new QuickAdapter<String>(R.layout.ccc, list) {
            @Override
            public void _convert(QuickHolder holder, String data) {
                holder.setText(R.id.tv_title, data).addOnClickListener(R.id.iv_head).addOnClickListener(R.id.tv_title);
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter_quick);
        */
/** 设置单击事件 *//*

        adapter_quick.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter_quick, View view, int position) {
                ToastUtils.showShort(position + "");
            }
        });
        */
/** 设置长按事件 *//*

        adapter_quick.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter_quick, View view, int position) {
                ToastUtils.showShort(list.get(position));
                //控制是否消耗点击事件，如果为false仍然会出发  单击事件
                return true;
            }
        });
        */
/** 设置子控件单击监听，需在convert里面add监听对象 *//*

        adapter_quick.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter_quick, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_head:
                        TextView textView = (TextView) adapter_quick.getViewByPosition(rv, position + adapter_quick.getHeaderLayoutCount(), R.id.tv_title);
                        ToastUtils.showShort("我是iv:" + textView.getText().toString());
                        break;
                    case R.id.tv_title:
                        ToastUtils.showShort("我是tv:" + position);
                        break;
                }
            }
        });
        */
/** item入场动画 *//*

        adapter_quick.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        */
/** 添加头部和底部布局，及点击处理方式 *//*

        View headView = View.inflate(this, R.layout.aaa, null);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("my name is headView");
            }
        });
        View footerView = View.inflate(this, R.layout.head, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("my name is footerView");
            }
        });
        //添加
        adapter_quick.addHeaderView(headView);
        adapter_quick.addFooterView(footerView);
        //删除
        */
/*adapter_quick.removeFooterView(footerView);
        adapter_quick.removeHeaderView(headView);
        adapter_quick.removeAllFooterView();
        adapter_quick.removeAllHeaderView();*//*

        */
/** 加载更多 *//*

        adapter_quick.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                addList();
            }
        }, rv);
        addList();
    }

    private void addList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (list.size() >= 10) {
                    adapter_quick.loadMoreFail();
                } else {
                    list.add("测试数据：" + 0);
                    adapter_quick.loadMoreComplete();
                }
            }
        }, 1000);
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        startActivity(new Intent(this, MainActivity.class));
    */
/*    addList();
        adapter_quick.notifyDataSetChanged();*//*

    }


  */
/*  @OnClick({R.id.open, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.open:
                Bundle bundle = new Bundle();
                bundle.putString("a", "aa");
                ActivityUtils.startActivity(bundle, this, SplashAty.class);
                break;
            case R.id.image:
                Glide.with(SplashAty.this).load("http://www.jj20.com/up/img/9445.jpg").apply(options).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LogUtils.e("11111");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LogUtils.e("22222");
                        if (resource instanceof BitmapDrawable) {
                            Bitmap bitmap = ImageUtils.fastBlur(((BitmapDrawable) resource).getBitmap(), 0.5f, 10);
                            img.setImageBitmap(bitmap);

                        }
                        return false;//监听加载，false将执行into，true不执行，标识是否已被消耗
                    }
                }).into(img);
                break;
        }
    }*//*

}
*/
