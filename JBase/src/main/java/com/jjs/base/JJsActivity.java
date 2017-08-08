package com.jjs.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.Permission.PermissionListener;
import com.jjs.base.Permission.PermissionSteward;
import com.jjs.base.mvp.BasePersenter;
import com.jjs.base.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：基础activity，fragment添加隐藏显示操作、回退栈操作、防止多次按下操作,返回2次退出，返回监听
 * Created by aa on 2017/6/13.
 */

public abstract class JJsActivity<P extends BasePersenter> extends RxAppCompatActivity implements PermissionListener {
    //fragmnet管理器
    private FragmentManager mFragmentManager;
    //所有fragment的集合
    private Map<Integer, List<Fragment>> mFragmentListMap;
    //触摸离开时
    private long EventDownTime;
    //是否需要进行判断重复点击
    private boolean hasCheckDouble = true;
    private static int CheckDoubleMillis = 200;
    public P mPersenter;//P层，具体aty中直接实例化即可

    /**
     * 对activity跳转返回进行判断，resultCode属于-1才处理
     *
     * @param requestCode
     * @param data
     */
    public abstract void onActivityResult(int requestCode, Intent data);

    /**
     * 权限申请成功时回调
     *
     * @param requestCode 请求码
     * @param grantList   申请通过的全部权限
     */
    public abstract void onPermissionSucceed(int requestCode, List<String> grantList);

    /**
     * 权限申请失败时回调
     *
     * @param requestCode 请求码
     * @param deniedList  没有申请通过的权限
     */
    public abstract void onPermissionFailed(int requestCode, List<String> deniedList);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        super.onDestroy();
        LoadingDialog.dissmiss();//关闭页面时释放dialog
        //清除P层对view的引用
        if (mPersenter != null)
            mPersenter.destroy();
    }

    long exitMillis;//上次返回键点击时间
    String exitToast;//返回提示语，根据是否为null判断是否需要进行判断

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();//屏蔽掉系统的返回事件，自行处理回退栈和finish
        popBackStackFinish();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //activity的点击事件，触摸事件由此分发，返回true时表示事件已消耗，不再向下传递
        //得考虑滑动触摸，所以在这里操作很难。
        if (hasCheckDouble) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - EventDownTime < CheckDoubleMillis) {
                    Log.i("JJsActivity", "触摸太过频繁，已屏蔽");
                    return true;
                } else {
                    EventDownTime = System.currentTimeMillis();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Store.TAG.RESULT_OK) {
            onActivityResult(requestCode, data);
        } else {
            Log.i("JJsActivity", "resultCode don't  -1");
        }
    }

    /********************************************************  权限申请方法  *****************************************************************/

    /**
     * 覆盖原有监听权限申请方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionSteward.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissionList) {
        onPermissionSucceed(requestCode, grantPermissionList);
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissionList) {
        onPermissionFailed(requestCode, deniedPermissionList);
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
