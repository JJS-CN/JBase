package com.jjs.demo;

/**
 * 本页：ptr刷新框架的自定义顶部参考用例
 *   compile 'in.srain.cube:ultra-ptr:1.0.11'
 *   使用PtrFrameLayout包裹控件
 * Created by jjs on 2017-04-12.
 * Email:994462623@qq.com
 */
 /*
 闪动文字的头部
 StoreHouseHeader storeHouseHeader = new StoreHouseHeader(this);
        //设置加载数据时显示的字符串，文本只支持0-9，英文字符
        storeHouseHeader.initWithString("999Loading...");
        TextView textView = new TextView(this);
        //设置头布局的背景颜色
        storeHouseHeader.setBackgroundColor(Color.BLACK);
        //设置文本的颜色
        storeHouseHeader.setTextColor(Color.WHITE);*/
    /* Ptr下拉框架参数
     ptr.setResistance(3.7f);//阻尼系数
        ptr.setRatioOfHeaderHeightToRefresh(1.2f);//触发刷新的头部移动比率（设置1.2倍的时候才能刷新）
        ptr.setDurationToClose(200);//回弹延时：回弹到刷新高度所用时间（默认200）
        ptr.setDurationToCloseHeader(1000);//头部回弹时间
        ptr.setPinContent(true); //内容不移动，只刷新出现
        // default is false
        ptr.setPullToRefresh(false); //下拉刷新 / 释放刷新（默认 释放刷新）
        // default is true
        ptr.setKeepHeaderWhenRefresh(true);//刷新时保持头部
            ptr.setHeaderView(demoPtrHeader);//添加头部控件
        ptr.addPtrUIHandler(demoPtrHeader);//添加头部的监听
        ptr.setPtrHandler  //设置下拉触发监听
    *
    * */
/*public class DemoPtrHeader extends LinearLayout  implements {
    Context mContext;

    public DemoPtrHeader(Context context) {
        super(context);
        init(context);
    }

    public DemoPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DemoPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setImageResource(com.jjs.base.R.drawable.pic_refresh1);
        this.addView(imageView);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.e("MMSS", "onUIReset");
        //头部隐藏状态下调用
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.e("MMSS", "onUIRefreshPrepare");
        //开始change时调用一次，需要走onUIReset重置后才会再次调用
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.e("MMSS", "onUIRefreshBegin");
        //可以进行刷新时调用，会自动调用ptr的setPtrHandler方法触发刷新
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.e("MMSS", "onUIRefreshComplete");
        //触发刷新完成后调用
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //  L.e("isUnderTouch:" + isUnderTouch + "=status:" + status + "=ptrIndicator:" + ptrIndicator.getCurrentPercent());
        //   Log.e("MMSS","onUIPositionChange");
        //头部移动状态下调用
    }
}*/
