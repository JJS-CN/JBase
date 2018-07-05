package com.jjs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jjs.base.base.BaseH5Activity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class XXXX extends BaseH5Activity {

    @BindView(R.id.web)
    WebView mWeb;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.frame_xxx)
    FrameLayout mLayout;

    @Override
    protected void _onReceivedTitle(WebView view, String title) {

    }

    @Override
    protected void _onProgressChanged(WebView view, int newProgress) {

    }

    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xxx);
        ButterKnife.bind(this);
        init(mWeb, "www.baidu.com");
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XXXX.this, XXXX.class));
            }
        });
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
       /* BaseDialogFragment.getInstance(R.layout.ccc, Color.MAGENTA)
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
                .show(getFragmentManager(), "1");*/
    }

 /*   @Override
    protected void onResume() {
        super.onResume();
        Dialog dialog=new AlertDialog.Builder(this).create();
        dialog.setContentView(R.layout.adapter_quick);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LoadingDialog.show();
            }
        });
        dialog.show();
    }*/


}
