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
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.R;
import com.jjs.base.mvp.BasePersenter;
import com.jjs.base.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
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
    //fragmnet管理器
    private FragmentManager mFragmentManager;
    //所有fragment的集合
    private Map<Integer, List<Fragment>> mFragmentListMap;
    //触摸离开时
    private long EventDownTime;
    //是否需要进行判断重复点击
    private boolean hasCheckDouble = true;
    private static int CheckDoubleMillis = 200;
    //是否需要进行左滑返回
    private boolean hasMovePopBack = false;
    private float movePopRangeX = 200;//横向移动多少距离触发返回
    float popDownX = 10000;//给予初始按下值，为了touch不被误判
    View prentView;
    //activity的切换动画
    private int animActivityOpen = R.anim.anim_activity_open;//默认activity的启动动画
    private int animActivityClose = R.anim.anim_activity_close;//默认activity的关闭动画
    private int animOpen;//临时启动动画
    private int animClose;//临时关闭动画
    private static boolean hasActivityAnim = true;//是否需要启动动画，全局属性建议在application中设定

    private int baseActivityBg = R.drawable.activity_bg;//默认activity的背景色
    private int ActivityBg = 0;//临时activity背景色

    private long exitMillis;//上次返回键点击时间
    private String exitToast;//返回提示语，根据是否为null判断是否需要进行判断

    public P mPersenter;//P层，具体aty中直接实例化即可

    // butterKinfe注解的对象
    protected Unbinder mUnbinder;


    /**
     * 对activity跳转返回进行判断，resultCode属于-1才处理
     *
     * @param requestCode
     * @param data
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
        getWindow().setBackgroundDrawable(getResources().getDrawable(ActivityBg == 0 ? baseActivityBg : ActivityBg));
    }

    //更新全局背景色
    public void initActivityBg(@DrawableRes int drawableId) {
        this.baseActivityBg = drawableId;
    }

    //设置临时背景色，优先于全局背景色
    public void updateActivityBg(@DrawableRes int drawableId) {
        this.ActivityBg = drawableId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        LoadingDialog.init(this);//创建dialog
        mFragmentManager = getFragmentManager();
        mFragmentListMap = new HashMap<>();//创建一个fragment集合，根据viewID保存hash集合中，可以根据viewid进行操作而不乱


    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.init(this);//用户页面返回时重建dialog
    }

    @Override
    protected void onDestroy() {
        LoadingDialog.dissmiss();//关闭页面时释放dialog
        //清除P层对view的引用
        if (mPersenter != null)
            mPersenter.destroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseStore.TAG.RESULT_OK) {
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

    List<Fragment> mFragmentResultList = new ArrayList<>();

    /**
     * 设置Fragment的回调
     */
    public void addFragmentResult(Fragment fragment) {
        mFragmentResultList.add(fragment);
    }

    /**
     * 设置需要进行按2下才推出程序的功能
     */
    public void hasExitDouble() {
        hasExitDouble(getString(R.string.JJS_exitToast));
    }

    public void hasExitDouble(String exitToast) {
        this.exitToast = exitToast;
    }

    /**
     * 设置返回监听,返回标识是否中断其他操作
     */
    public boolean onBackListener() {
        return false;
    }


    /**
     * fragment回退栈管理（返回时优先处理回退栈推出）
     */
    public void popBackStackFinish() {
        //设置返回键监听,返回标识是否中断其他操作
        if (onBackListener()) {
            return;
        }
        //按2次才能退出的相关代码
        if (exitToast != null && System.currentTimeMillis() - exitMillis > 2000) {
            ToastUtils.showShort(exitToast);
            exitMillis = System.currentTimeMillis();
            return;
        }
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
    public void initActivityAnim(@AnimRes int animOpen, @AnimRes int animClose) {
        animActivityOpen = animOpen;
        animActivityClose = animClose;
    }

    /**
     * 设置临时，本activity的启动和关闭动画
     */
    public void updateActivityAnim(@AnimRes int animOpen, @AnimRes int animClose) {
        this.animOpen = animOpen;
        this.animClose = animClose;
    }

    /**
     * 设置是否需要动画
     * 可被updateActivityAnim方法绕过，执行动画
     */
    public static void setHasActivityAnim(boolean hasAnim) {
        hasActivityAnim = hasAnim;
    }

    /**
     * 重写start方法，加入启动动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //如果2个不设置全，activity切换时会造成短暂黑屏
        if (animOpen != 0 || hasActivityAnim)
            this.overridePendingTransition(animOpen != 0 ? animOpen : animActivityOpen, R.anim.anim_activity_null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (animOpen != 0 || hasActivityAnim)
            this.overridePendingTransition(animOpen != 0 ? animOpen : animActivityOpen, R.anim.anim_activity_null);
    }

    /**
     * 重写finish方法，键入关闭动画
     */
    @Override
    public void finish() {
        super.finish();
        if (animClose != 0 || hasActivityAnim)
            this.overridePendingTransition(R.anim.anim_activity_null, animClose != 0 ? animClose : animActivityClose);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();//屏蔽掉系统的返回事件，自行处理回退栈和finish
        popBackStackFinish();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //activity的点击事件，触摸事件由此分发，返回true时表示事件已消耗，不再向下传递
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (hasCheckDouble) {
                if (System.currentTimeMillis() - EventDownTime < CheckDoubleMillis) {
                    Log.e("BaseActivity", "this Event has ignore,because you touch too much! ");
                    return true;
                } else {
                    EventDownTime = System.currentTimeMillis();
                }
            }
            if (hasMovePopBack && ev.getX() <= movePopRangeX) {
                popDownX = ev.getX();
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            /**
             * 判断是否需要左滑返回
             */
            if (hasMovePopBack && popDownX <= movePopRangeX) {
                //抬起，判断是否需要返回
                if (ev.getX() - popDownX > movePopRangeX) {
                    //执行关闭操作
                    popBackStackFinish();
                } else {
                    //执行回弹操作
                    if (prentView == null) {
                        prentView = this.getWindow().getDecorView();
                    }
                    ValueAnimator animator = ValueAnimator.ofFloat(prentView.getX(), 0);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            prentView.setX((Float) animation.getAnimatedValue());
                        }
                    });
                    animator.setDuration(200).start();
                }
                popDownX = 10000;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            //左滑返回的拖动效果
            if (hasMovePopBack) {
                if (prentView == null) {
                    prentView = this.getWindow().getDecorView();
                }
                prentView.setX(ev.getX() - popDownX < 0 ? 0 : ev.getX() - popDownX);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置是否打开左侧滑动返回上一层
     */
    public void setHasMovePopBack(boolean hasMovePopBack) {
        this.hasMovePopBack = hasMovePopBack;
    }

    /**
     * 设置是否打开多次触碰的检测
     *
     * @param hasCheckDouble 是否检测多次触碰
     */
    public void setCheckDoubleEnabled(boolean hasCheckDouble) {
        this.hasCheckDouble = hasCheckDouble;
    }

    /**
     * 设置最短触碰间隔，如果开启触碰检测的话才有效
     *
     * @param doubleMillis 2次触碰之间的间隔
     */
    public void setCheckDoubleMillis(int doubleMillis) {
        this.CheckDoubleMillis = doubleMillis;
    }


    /********************************************************  点击空白隐藏软键盘  *****************************************************************/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideInputMethod();
        }
        return super.onTouchEvent(event);
    }

    public void hideInputMethod() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /********************************************************  权限申请方法  *****************************************************************/

    /**
     * 覆盖原有监听权限申请方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    /***********************************************   以下是对fragment的操作封装   *****************************************************************************/

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
