package com.jjs.demo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.jjs.R;
import com.jjs.base.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 说明：
 * Created by aa on 2018/1/3.
 */

public class AAFragment extends BaseFragment {


    @BindView(R.id.iv_1)
    ImageView mIv1;

    @Override
    protected int getLayoutId() {
        return R.layout.head;
    }

    @Override
    protected void initData(Bundle bundle) {
        mIv1.setImageResource(R.mipmap.ic_launcher);

    }


    @OnClick(R.id.iv_1)
    public void onViewClicked() {
        Toast.makeText(getActivity(), "111", Toast.LENGTH_SHORT).show();
    }
}
