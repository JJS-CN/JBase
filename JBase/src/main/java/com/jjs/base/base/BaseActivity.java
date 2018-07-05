package com.jjs.base.base;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jjs.base.R;
import com.jjs.base.mvp.BasePersenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * 说明：基础activity，fragment添加隐藏显示操作、回退栈操作、防止多次按下操作,返回2次退出，返回监听；
 * 手动实例化P层并赋予给mPersenter变量，然后调用；
 * Butterkinfe.bind(this); 之后，手动赋予给变量 mUnbinder，将会自动在onDestroy中进行unbind；
 * <p>
 * 过渡动画准备：Pair.create(view, key); 创建一条Pair 或只有一个view时直接设置name；
 * activity需要设定theme，theme继承AppCompat,默认空白，v21以上设置 <item name="android:windowContentTransitions">true</item>
 * 这样设置theme准备后，api21以下为默认跳转，以上为动画
 * 将Pair准备成bundle： ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairA, pairB).toBundle()
 * 发送过渡动画： ActivityCompat.startActivity(this, intent, bundle);
 * 接收过渡动画：ViewCompat.setTransitionName(view, key); 这里key值必须和发送放一致
 * 过渡动画注意：接受方从intent拿到view展示内容已达到效果；activity需要配置theme
 * Created by aa on 2017/6/13.
 */

public abstract class BaseActivity<P extends BasePersenter> extends RxAppCompatActivity {
    public BaseActivity mActivity;
    /***  屏幕宽高  ***/
    public int mWindowWidth = -1;
    public int mWindowHeight = -1;
    /***  重复点击拦截  ***/
    public boolean hasClicksInspect = true;
    public int mClicksInspectMillis = 200;//重复点击间隔---毫秒
    private long mClicksInspectLastTime;//记录上次点击时间
    /***  左滑关闭界面  ***/
    public boolean hasLeftMoveExit = false;
    public float mLeftMoveTrigger = 0.2f;//默认屏幕左侧0.2f触发左滑，根据屏幕宽度动态计算
    private float mLeftMoveDownX = -1;//初始按下值
    /***  连点物理back关闭界面(默认2000毫秒)  ***/
    public boolean hasDoubleClickExit = false;
    public String mDoubleClickExitContent = "再按一次退出程序";//提示语
    private long mDoubleClickExitLastTime;//上次返回键点击时间

    /***  activity的切换动画  ***/
    public boolean hasActivityAnim = true;//是否需要切换动画
    private static int DEFAULT_ANIM_OPEN = R.anim.anim_activity_open;//默认activity的启动动画
    private static int DEFAULT_ANIM_CLOSE = R.anim.anim_activity_close;//默认activity的关闭动画
    private int mActivityAnimOpen;//临时启动动画（优先于默认动画）
    private int mActivityAnimClose;//临时关闭动画（优先于默认动画）
    /***  Fragment回调处理  ***/
    public boolean hasEventHideInput = true;//是否在点击事件未被消耗情况关闭软件盘
    private List<Fragment> mFragmentResultList = new ArrayList<>();//fragment回调监听
    /***  Activity统一背景色  ***/
    public boolean hasBackgroundColor = true;//是否设置背景色
    private static int DEFAULT_BACKGROUND_COLOR = R.drawable.activity_bg;//默认activity的背景色


    public P mPersenter;//P层，具体aty中直接实例化即可
    protected Unbinder mUnbinder;// butterKinfe注解的对象


    /**
     * 对activity跳转返回进行判断，resultCode属于-1才处理
     */
    protected abstract void onActivityResult(int requestCode, Intent data);


    /**
     * 重写，默认执行butterkinfe绑定操作。
     * butterkinfe会自动新建onCreate，所以必须实现onCreate方法
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
        /**
         * 由于activity切换时会有黑屏现象，所以默认theme设置为透明背景
         * 但在加载完xml之后需要重新设置背景色,通过在setContentView之前调用init或update方法更新背景色
         */
        if (hasBackgroundColor)
            getWindow().setBackgroundDrawable(getResources().getDrawable(DEFAULT_BACKGROUND_COLOR));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //过渡动画需要配置这个
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        mActivity = this;
        mFragmentManager = getFragmentManager();
        mFragmentListMap = new HashMap<>();//创建一个fragment集合，根据viewID保存hash集合中，可以根据viewid进行操作而不乱
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWindowWidth = dm.widthPixels;
        mWindowHeight = dm.heightPixels;
    }


    @Override
    protected void onDestroy() {
        //清除P层对view的引用
        if (mPersenter != null)
            mPersenter.destroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroy();

    }


    /**
     * fragment中启动activity有时候不会监听到回调，这里设置一下设置Fragment的回调
     */
    public void addFragmentResult(Fragment fragment) {
        mFragmentResultList.add(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onActivityResult(requestCode, data);
        } else {
            Log.e("BaseActivity", "resultCode don't  -1");
        }
        /**
         * 进行Fragment的回调处理
         */
        try {
            for (int i = 0; i < mFragmentResultList.size(); i++) {
                mFragmentResultList.get(i).onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * fragment回退栈管理（返回时优先处理回退栈推出）
     */
    public void popBackStackFinish() {
        if (hasDoubleClickExit) {
            if (mDoubleClickExitContent != null && System.currentTimeMillis() - mDoubleClickExitLastTime > 2000) {
                if (mDoubleClickExitContent == null) {
                    Toast.makeText(this, mDoubleClickExitContent, Toast.LENGTH_SHORT).show();
                }
                mDoubleClickExitLastTime = System.currentTimeMillis();
                return;
            }
        }
        //按2次才能退出的相关代码

        //当回退栈中有数据时，进行fragment的回退
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
            return;
        }
        //否则直接finish
        finish();
    }
    /*********************************************************  acitivity切换动画相关  *************************************************************/
    /**
     * 设置全局activity的启动动画、关闭动画；打开调用、关闭调用
     */
    public void initActivityAnim(boolean hasAnim, @AnimRes int defaultAnimOpen, @AnimRes int defaultAnimClose) {
        hasActivityAnim = hasAnim;
        DEFAULT_ANIM_OPEN = defaultAnimOpen;
        DEFAULT_ANIM_CLOSE = defaultAnimClose;
    }

    /**
     * 设置临时，本activity的启动和关闭动画,只要设置，将会使用到
     */
    public void setActivityAnim(@AnimRes int animOpen, @AnimRes int animClose) {
        this.mActivityAnimOpen = animOpen;
        this.mActivityAnimClose = animClose;
    }

    /**
     * 重写start方法，加入启动动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //如果2个不设置全，activity切换时会造成短暂黑屏
        if (mActivityAnimOpen != 0 || hasActivityAnim)
            this.overridePendingTransition(mActivityAnimOpen != 0 ? mActivityAnimOpen : DEFAULT_ANIM_OPEN, R.anim.anim_activity_null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (mActivityAnimOpen != 0 || hasActivityAnim)
            this.overridePendingTransition(mActivityAnimOpen != 0 ? mActivityAnimOpen : DEFAULT_ANIM_OPEN, R.anim.anim_activity_null);
    }

    /**
     * 重写finish方法，键入关闭动画
     */
    @Override
    public void finish() {
        super.finish();
        if (mActivityAnimClose != 0 || hasActivityAnim)
            this.overridePendingTransition(R.anim.anim_activity_null, mActivityAnimClose != 0 ? mActivityAnimClose : DEFAULT_ANIM_CLOSE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();//屏蔽掉系统的返回事件，自行处理回退栈和finish
        popBackStackFinish();
    }

    /**************************************  触摸相关  ******************************************/
    private View mDecorView;//用于左滑动画处理

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**********  快速重复点击拦截  **********/
        if (hasClicksInspect) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - mClicksInspectLastTime < mClicksInspectMillis) {
                    Log.e("BaseActivity", "this Event has ignore,because you touch too much! ");
                    return true;
                } else {
                    mClicksInspectLastTime = System.currentTimeMillis();
                }
            }
        }
        /***********  左滑back逻辑  ***********/
        if (hasLeftMoveExit) {
            if (mDecorView == null) {
                mDecorView = this.getWindow().getDecorView();
            }
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (ev.getX() <= mLeftMoveTrigger * mWindowWidth) {
                    mLeftMoveDownX = ev.getX();
                }
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                //左滑返回的拖动效果
                if (mLeftMoveDownX != -1) {
                    float moveX = ev.getX() - mLeftMoveDownX;
                    mDecorView.setX(moveX < 0 ? 0 : moveX);
                }
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mLeftMoveDownX != -1 && mLeftMoveDownX <= mLeftMoveTrigger * mWindowWidth) {
                    if (ev.getX() - mLeftMoveDownX > mLeftMoveTrigger * mWindowWidth) {
                        //执行关闭操作
                        popBackStackFinish();
                    } else {
                        //执行回弹操作
                        ValueAnimator animator = ValueAnimator.ofFloat(mDecorView.getX(), 0);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mDecorView.setX((Float) animation.getAnimatedValue());
                            }
                        });
                        animator.setDuration(200).start();
                    }
                    mLeftMoveDownX = -1;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasEventHideInput && event.getAction() == MotionEvent.ACTION_DOWN) {
            hideInputMethod();
        }
        return super.onTouchEvent(event);
    }

    /*******  点击空白隐藏软键盘  *************/
    public void hideInputMethod() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /***********************************************   以下是对fragment的操作封装   *****************************************************************************/
    //fragmnet管理器
    private FragmentManager mFragmentManager;
    //所有fragment的集合
    private Map<Integer, List<Fragment>> mFragmentListMap;

    /**
     * 获取fragment操作的事务
     *
     * @return
     */
    public FragmentTransaction getTransaction() {
        return mFragmentManager.beginTransaction();
    }

    /**
     * 获取对应id下的fragmnet集合
     */
    public List<Fragment> getFragmentList(int containerViewId) {
        List<Fragment> fragmentList = mFragmentListMap.get(containerViewId);
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        return fragmentList;
    }

    /**
     * 添加fragmnet进入
     *
     * @param containerViewId fragment添加的viewId
     * @param fragment        具体的fragment
     * @param tag             fragment对应的tag
     * @param addStack        是否添加到回退栈
     */
    public void addFragment(@IdRes int containerViewId, Fragment fragment, String tag, boolean addStack) {
        FragmentTransaction transaction = getTransaction();
       /* if (addStack) {
            //为true时才进行动画
            //1参数为进入动画，4参数为出去动画，中间2个好像无效
            transaction.setCustomAnimations(R.anim.h_fragment_enter, R.anim.h_fragment_exit, R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit);
        }*/
        transaction.add(containerViewId, fragment, tag);
        //看看是否添加到回退栈，全部交由实际控制，一般第一个fragment都不添加回退栈，会造成空白\
        if (addStack)
            transaction.addToBackStack(null);
        transaction.commit();
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        fragmentList.add(fragment);
        mFragmentListMap.put(containerViewId, fragmentList);
    }

    /**
     * 添加多个fragment，默认不设置tag，默认不添加回退栈
     */
    public void addFragments(@IdRes int containerViewId, Fragment... fragment) {
        FragmentTransaction transaction = getTransaction();
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        for (int i = 0; i < fragment.length; i++) {
            transaction.add(containerViewId, fragment[i]);
            fragmentList.add(fragment[i]);
        }
        transaction.commit();
        mFragmentListMap.put(containerViewId, fragmentList);
    }

    /**
     * 根据tag和viewId显示一个fragment
     *
     * @param containerViewId fragment添加的viewId
     * @param tag             添加这个fragment时设定的tag
     */
    public void showFragment(int containerViewId, String tag) {
        for (Fragment fragment : getFragmentList(containerViewId)) {
            if (fragment.getTag().equals(tag)) {
                getTransaction().show(fragment).commit();
                break;
            }
        }
    }

    /**
     * 根据当前viewId中所持有的fragment集合中的下标进行显示
     *
     * @param containerViewId fragment添加的viewId
     * @param position        第几个添加进入的下标
     */
    public void showFragment(int containerViewId, int position) {
        getTransaction().show(getFragmentList(containerViewId).get(position)).commit();
    }

    /**
     * 根据tag显示viewId里的一个fragment，其他全部隐藏
     */
    public void showFragmentOne(int containerViewId, String tag) {
        FragmentTransaction transaction = getTransaction();
        for (Fragment fragment : getFragmentList(containerViewId)) {
            if (fragment.getTag().equals(tag)) {
                transaction.show(fragment);
            } else if (!fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    /**
     * 根据下标显示viewId里的一个fragment，其他全部隐藏
     */
    public void showFragmentOne(int containerViewId, int position) {
        FragmentTransaction transaction = getTransaction();
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == position) {
                transaction.show(fragmentList.get(i));
            } else {
                transaction.hide(fragmentList.get(i));
            }
        }
        transaction.commit();
    }

    /**
     * 根据下标隐藏一个fragment
     */
    public void hideFragment(int containerViewId, int position) {
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        getTransaction().hide(fragmentList.get(position)).commit();
    }

    /**
     * 根据tag隐藏一个fragment
     */
    public void hideFragment(int containerViewId, String tag) {
        for (Fragment fragment : getFragmentList(containerViewId)) {
            if (fragment.getTag().equals(tag)) {
                getTransaction().hide(fragment).commit();
                break;
            }
        }
    }

    /**
     * 根据下标删除一个fragment
     */
    public void removeFragment(int containerViewId, int position) {
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        getTransaction().remove(fragmentList.get(position)).commit();
        fragmentList.remove(position);
    }

    /**
     * 根据tag删除一个ftagment
     */
    public void removeFragment(int containerViewId, String tag) {
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i).getTag().equals(tag)) {
                getTransaction().remove(fragmentList.get(i)).commit();
                fragmentList.remove(i);
                break;
            }
        }
    }


    /**
     * 根据tag替换一个fragment
     *
     * @param containerViewId viewID
     * @param newFragment     新fragment
     * @param tag             旧
     * @param addStack
     */
    public void replaceFragment(@IdRes int containerViewId, Fragment newFragment, String tag, boolean addStack) {
        List<Fragment> fragmentList = getFragmentList(containerViewId);
        FragmentTransaction transaction = getTransaction();
        for (Fragment fragment : fragmentList) {
            transaction.remove(fragment);
        }
        transaction.commit();
        fragmentList.clear();
        addFragment(containerViewId, newFragment, tag, addStack);
    }


}
