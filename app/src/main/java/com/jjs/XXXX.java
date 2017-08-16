package com.jjs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jjs.base.http.download.DownLoadObserver;
import com.jjs.base.http.download.DownloadInfo;
import com.jjs.base.http.download.DownloadManager;

import io.reactivex.annotations.NonNull;


public class XXXX extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xxx);
        DownloadManager.getInstance().download("http://gdown.baidu.com/data/wisegame/93812d86a2e7cd82/aiqiyi_80910.apk", new DownLoadObserver() {
            @Override
            public void _onNext(DownloadInfo downloadInfo) {
                Log.e("download", downloadInfo.getProgress() + "/" + downloadInfo.getTotal()  + "");
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
        });

    }

}
