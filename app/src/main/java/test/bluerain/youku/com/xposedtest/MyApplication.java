package test.bluerain.youku.com.xposedtest;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Project: XposedTest.
 * Data: 2016/2/19.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d("Xposed", "Applacation had created .....................................................");
    }

    public static Context getContext() {
        return mContext;
    }
}
