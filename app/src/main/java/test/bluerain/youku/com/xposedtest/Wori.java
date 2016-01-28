package test.bluerain.youku.com.xposedtest;

import android.text.TextUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import test.bluerain.youku.com.xposedtest.hooks.InputStreamHook;
import test.bluerain.youku.com.xposedtest.hooks.LocationMangerHook;
import test.bluerain.youku.com.xposedtest.hooks.OutputStreamHook;
import test.bluerain.youku.com.xposedtest.hooks.RuntimeHook;
import test.bluerain.youku.com.xposedtest.hooks.TelephoneHook;

/**
 * Project: XposedTest.
 * Data: 2016/1/14.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class Wori implements IXposedHookLoadPackage {
    public static final String TAG = "Xposed";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.autonavi.minimap
        XposedBridge.log("wohhhhhh----------------------------->" + loadPackageParam.packageName);
//        if (!TextUtils.equals(loadPackageParam.packageName, "test.bluerain.youku.com.des"))
//        if (!TextUtils.equals(loadPackageParam.packageName, "com.autonavi.minima"))  //高德地图
//        if (!TextUtils.equals(loadPackageParam.packageName, "xiaomeng.bupt.com.demo"))
        if (!TextUtils.equals(loadPackageParam.packageName, "com.ubercab"))
            return;

//        XposedBridge.hookAllMethods(LocationManager.class, "requestLocationUpdates", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////                XposedBridge.log("hooked method requestLocationUpdates");
//                Log.d(TAG, "hooked method requestLocationUpdates", new Exception());
//                Object[] args = param.args;
//                for (Object obj : args) {
//                    if (null != obj) {
//                        XposedBridge.log("arg is " + obj.toString());
//                    }
//                }
//                super.beforeHookedMethod(param);
//            }
//        });
////
        HookManger.addHooks(new TelephoneHook());
        HookManger.addHooks(new RuntimeHook());
        HookManger.addHooks(new LocationMangerHook());
        HookManger.addHooks(new OutputStreamHook());
        HookManger.addHooks(new InputStreamHook());
        HookManger.startHook(loadPackageParam.classLoader);

    }
}
