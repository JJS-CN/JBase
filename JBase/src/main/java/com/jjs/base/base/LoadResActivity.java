package com.jjs.base.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.jjs.base.R;

/**
 * 说明：用于分包处理作用，未测试
 * Created by aa on 2017/6/19.
 */

public class LoadResActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.anim_activity_null, R.anim.anim_activity_null);
        setContentView(R.layout.layout_load);
        new LoadDexTask().execute();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                Log.d("loadDex", "install finish");
                ((BaseApplication) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                Log.e("loadDex", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("loadDex", "get install finish");
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        //cannot backpress
    }
}