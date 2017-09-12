package com.jjs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jjs.base.widget.BaseDialogFragment;

import butterknife.ButterKnife;


public class XXXX extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xxx);
        ButterKnife.bind(this);
        /*DownloadManager.getInstance().download("http://gdown.baidu.com/data/wisegame/93812d86a2e7cd82/aiqiyi_80910.apk", new DownLoadObserver() {
            @Override
            public void _onNext(DownloadInfo downloadInfo) {
                Log.e("download", downloadInfo.getProgress() + "/" + downloadInfo.getTotal() + "");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e("download", "complete");
            }

            @Override
            public long setTimeMillis() {
                return 2000;
            }
        });*/
       /* Dialog dialog = new BaseDialog(this)
                .setView(R.layout.ccc)
                .setClick(R.id.tv_title)
                .setListener(new BaseDialog.OnBaseClickListener() {
                    @Override
                    public void onClick(View rootView, View checkView) {
                        switch (checkView.getId()) {
                            case R.id.tv_title:
                                Toast.makeText(XXXX.this, "dianji!!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .create();
        dialog.show();*/
        BaseDialogFragment.getInstance(R.layout.ccc, Color.MAGENTA)
                .setInitViewListener(new BaseDialogFragment.OnInitViewListener() {
                    @Override
                    public void onInit(BaseDialogFragment.ViewHolder mHolder) {
                        mHolder.setText(R.id.tv_title, "xx修改后！！！");
                        mHolder.setClick(R.id.tv_title);
                        mHolder.setDismiss(R.id.tv_title);
                    }
                })
                .setBaseClickListener(new BaseDialogFragment.OnBaseClickListener() {
                    @Override
                    public void onClick(BaseDialogFragment.ViewHolder mHolder, View v) {
                        switch (v.getId()) {
                            case R.id.tv_title:
                                Toast.makeText(XXXX.this, "!11111111", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                })
                .hasTranSparent(false)
                .hasCancelable(false)
                .show(getFragmentManager(), "1");
    }

}
