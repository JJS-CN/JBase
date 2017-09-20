package com.jjs.base.utils.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jjs.base.R;
import com.jjs.base.widget.ShapeView;

import java.util.ArrayList;
import java.util.List;


/**
 * 说明：
 * Created by aa on 2017/7/10.
 */

public class PagerUtils {
    private static DotsData dotsData = new DotsData();
    private static Context context;
    ViewPager viewPager;//滑动轮播
    LinearLayout ll_dot;//小圆点显示控件
    ImageView iv_bg;//整体背景图片

    JJsPagerAdapter adapter;//适配器
    List<View> views;
    List<View> dots;

    boolean isAutoPlay;//是否自动轮播
    boolean isInfinite;//是否无限轮播
    boolean isDocCheck;//是否需要doc点击切换效果
    int autoPlayMillis;//轮播间隔
    boolean changeBG = false;//是否修改背景
    float moveBGF;//移动百分比
    int moveBG;//移动精确距离dp

    /**
     * 设置展示控件
     */
    public PagerUtils setView(Context context, ViewPager viewPager, LinearLayout ll_dot, ImageView iv_bg, List<View> views) {
        this.viewPager = viewPager;
        this.ll_dot = ll_dot;
        this.iv_bg = iv_bg;
        this.views = views;
        this.context = context;
        return this;
    }

    /**
     * 轮播控制
     *
     * @param isAutoPlay     是否开启自动轮播
     * @param isInfinite     是否开启无限轮播
     * @param autoPlayMillis 轮播间隔
     */
    public PagerUtils setAutoPlay(boolean isAutoPlay, boolean isInfinite, int autoPlayMillis) {
        this.isAutoPlay = isAutoPlay;
        this.isInfinite = isInfinite;
        this.autoPlayMillis = autoPlayMillis;
        return this;
    }

    /**
     * 设置是否点击按钮切换viewpager页签
     */
    public PagerUtils setDocCheck(boolean isDocCheck) {
        this.isDocCheck = isDocCheck;
        return this;
    }

    /**
     * 单独view的点击事件
     */
    OnItemClickListener onItemClickListener;

    public PagerUtils setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
        return this;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 设置切换动画效果
     *
     * @param reverseDrawingOrder true表示提供的PageTransformer画view时是倒序，false则是正序（子view的绘制位置会有所改变）
     * @param transformer         具体切换动画效果
     */
    public PagerUtils setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    /**
     * 输入背景移动的距离
     * 背景会被裁剪缩放
     *
     * @param moveBG dp
     */
    public PagerUtils setMoveBG(int moveBG) {
        this.moveBG = moveBG;
        this.moveBGF = 0;
        return this;
    }

    public PagerUtils setMoveBG(float moveBGF) {
        //百分比
        this.moveBGF = moveBGF;
        this.moveBG = 0;
        return this;
    }

    public void setData(List<View> views) {
        this.views = views;
        adapter.notifyDataSetChanged();
    }


    /**
     * 进行创建绑定数据操作
     */
    public void create() {
        //适配器
        adapter = new JJsPagerAdapter(views, isInfinite);
        viewPager.setAdapter(adapter);
        //小圆点
        dots = new ArrayList<>();
        ll_dot.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            final int finalI1 = i;
            ShapeView dotView = new ShapeView(viewPager.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) dp2px(dotsData.dotRadius) * 2, (int) dp2px(dotsData.dotRadius) * 2);
            int dotmargin = (int) dp2px(dotsData.dotMargin);
            params.setMargins(dotmargin, dotmargin, dotmargin, dotmargin);
            dotView.setLayoutParams(params);
            if (i == 0) {
                dotView.setShapeType(1).setSolidColor(dotsData.dotCheckColor).setRadius(dp2px(dotsData.dotRadius)).show();
            } else {
                dotView.setShapeType(1).setSolidColor(dotsData.dotDefaultColor).setRadius(dp2px(dotsData.dotRadius)).show();
            }
            if (isDocCheck)
                dotView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(finalI1);
                    }
                });
            ll_dot.setOrientation(LinearLayout.HORIZONTAL);
            ll_dot.addView(dotView);
            if (onItemClickListener != null) {
                final int finalI = i;
                views.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(finalI);
                    }
                });
            }
        }
        //设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //循环轮播时，背景滑动不好控制，所以return
                if (isInfinite || iv_bg == null)
                    return;
                if (moveBGF != 0)
                    setMoveBG((int) (moveBGF * iv_bg.getWidth()));
                if (moveBG != 0) {
                    if (!changeBG) {
                        changeBG = true;
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(iv_bg.getWidth() + (int) dp2px(moveBG) * (viewPager.getChildCount() + 1), ViewGroup.LayoutParams.MATCH_PARENT);
                        iv_bg.setLayoutParams(params);
                    }
                    if (positionOffset != 0)
                        iv_bg.setTranslationX(-(positionOffset * dp2px(moveBG) + position * dp2px(moveBG)));
                }
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ll_dot.getChildCount(); i++) {
                    ShapeView shapeView = (ShapeView) ll_dot.getChildAt(i);
                    if (i == position % ll_dot.getChildCount()) {
                        shapeView.setSolidColor(dotsData.dotCheckColor).show();
                    } else {
                        shapeView.setSolidColor(dotsData.dotDefaultColor).show();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //轮播控制
        if (isAutoPlay) {
            //如果需要自动轮播
            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, autoPlayMillis);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //由于
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        handler.sendEmptyMessageDelayed(1, autoPlayMillis);
                    } else if (handler.hasMessages(1)) {
                        handler.removeMessages(1);
                    }
                    return false;
                }
            });
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            if (isInfinite || viewPager.getCurrentItem() < viewPager.getChildCount())
                handler.sendEmptyMessageDelayed(1, autoPlayMillis);
        }
    };

    /**
     * 控制是否展示小圆点
     *
     * @param isShowDot 是否展示小圆点，默认显示
     */
    public void setShowDots(boolean isShowDot) {
        ll_dot.setVisibility(isShowDot ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 初始化dot参数
     *
     * @param dotRadius       圆点半径，大小
     * @param dotMargin       圆点marin，外边距
     * @param dotCheckColor   选中圆点颜色
     * @param dotDefaultColor 默认圆点颜色
     */
    public static void initDots(int dotRadius, int dotMargin, @ColorInt int dotCheckColor, @ColorInt int dotDefaultColor) {
        dotsData = new DotsData().initDots(dotRadius, dotMargin, dotCheckColor, dotDefaultColor);
    }


    /**
     * builder是为了配置基础static参数，动态参数不应由builder配置
     */
    private static class DotsData {
        int dotCheckColor = Color.WHITE, dotDefaultColor = Color.LTGRAY, dotRadius = 4, dotMargin = 5;

        /**
         * 小圆点初始化
         *
         * @param dotRadius       圆点半径，大小
         * @param dotMargin       圆点marin，外边距
         * @param dotCheckColor   选中圆点颜色
         * @param dotDefaultColor 默认圆点颜色
         */
        public DotsData initDots(int dotRadius, int dotMargin, @ColorInt int dotCheckColor, @ColorInt int dotDefaultColor) {
            this.dotRadius = dotRadius;
            this.dotMargin = dotMargin;
            this.dotCheckColor = dotCheckColor;
            this.dotDefaultColor = dotDefaultColor;
            return this;
        }
    }


    /**
     * 说明：适配器
     * Created by aa on 2017/7/6.
     */

    public class JJsPagerAdapter extends PagerAdapter {

        private List<View> views;//展示视图
        private boolean isInfinite;//是否开启无限轮播


        public JJsPagerAdapter(List<View> views) {
            new JJsPagerAdapter(views, false);
        }


        /**
         * 初始化适配器
         *
         * @param views      view视图集合
         * @param isInfinite 是否开启无限轮播
         */
        public JJsPagerAdapter(final List<View> views, boolean isInfinite) {
            super();
            RequestOptions options = new RequestOptions().centerCrop();
            this.views = new ArrayList<>();
            if (views.size() == 1) {
                for (int i = 0; i < 4; i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            views.get(0).performClick();
                        }
                    });

                    Glide.with(context).load(views.get(0).getTag(R.id.tag_banner_url)).apply(options).into(imageView);
                    this.views.add(imageView);
                }
            } else if (views.size() == 2) {
                for (int i = 0; i < 2; i++) {
                    ImageView imageView = new ImageView(context);
                    Glide.with(context).load(views.get(0).getTag(R.id.tag_banner_url)).apply(options).into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            views.get(0).performClick();
                        }
                    });
                    this.views.add(imageView);
                    ImageView imageView1 = new ImageView(context);
                    Glide.with(context).load(views.get(1).getTag(R.id.tag_banner_url)).apply(options).into(imageView1);
                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            views.get(1).performClick();
                        }
                    });
                    this.views.add(imageView1);
                }
            } else {
                this.views = views;
            }
            this.isInfinite = isInfinite;
        }

        @Override
        public int getCount() {
            return isInfinite ? Integer.MAX_VALUE : views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup parent = (ViewGroup) views.get(position % views.size()).getParent();
            if (parent != null) {
                parent.removeView(views.get(position % views.size()));
            }
            container.addView(views.get(position % views.size()), 0);
            return views.get(position % views.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView(views.get(position % views.size()));
        }
    }

    public static float dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
