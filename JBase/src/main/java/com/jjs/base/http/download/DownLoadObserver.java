package com.jjs.base.http.download;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 说明：下载类DownloadManager使用的监听回调方法
 * 实例化时，强制需要复写方法“开始、错误、结束”
 * Created by aa on 2017/8/16.
 */

public abstract class DownLoadObserver implements Observer<DownloadInfo> {
    protected Disposable d;//可以用于取消注册的监听者
    protected DownloadInfo downloadInfo;
    long lastTime;//上次log时间，进行差值判断，防止调用太过频繁导致UI重绘压力重大

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(DownloadInfo downloadInfo) {
        if (System.currentTimeMillis() - lastTime > setTimeMillis()) {
            this.downloadInfo = downloadInfo;
            _onNext(downloadInfo);
            lastTime = System.currentTimeMillis();
        }
    }

    /**
     * 设置onNext调用间隔，子类复写方法即可
     */
    public long setTimeMillis() {
        return 200;
    }

    public abstract void _onNext(DownloadInfo downloadInfo);
}