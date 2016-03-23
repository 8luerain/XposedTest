package test.bluerain.youku.com.xposedtest;

import android.os.*;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.hooks.BaseHook;
import test.bluerain.youku.com.xposedtest.hooks.InputStreamHook;
import test.bluerain.youku.com.xposedtest.hooks.LocationMangerHook;
import test.bluerain.youku.com.xposedtest.hooks.OutputStreamHook;
import test.bluerain.youku.com.xposedtest.hooks.RuntimeHook;
import test.bluerain.youku.com.xposedtest.hooks.SettingHook;
import test.bluerain.youku.com.xposedtest.hooks.TelephoneHook;
import test.bluerain.youku.com.xposedtest.hooks.WifiHook;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Project: XposedTest.
 * Data: 2016/1/14.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class Wori implements IXposedHookLoadPackage {
    public static final String TAG = "Xposed";

    public RandomBean bean;

    public Wori() {
        Log.d("TAG", "Wori class created ......................");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
//        XposedBridge.log("loading -------> " + loadPackageParam.packageName +" & pid is " + android.os.Process.myPid());
        Log.d("process ","loading -------> " + loadPackageParam.packageName +" & pid is " + android.os.Process.myPid());

//        if (!TextUtils.equals(loadPackageParam.packageName, "test.bluerain.youku.com.des"))
//        if (!TextUtils.equals(loadPackageParam.packageName, "com.autonavi.minima"))  //高德地图
//        if (!TextUtils.equals(loadPackageParam.packageName, "xiaomeng.bupt.com.demo"))
//        if (!TextUtils.equals(loadPackageParam.packageName, "com.sankuai.meituan"))
        if (!TextUtils.equals(loadPackageParam.packageName, "com.ubercab")) {
            return;
        }
        bean = CommonUtils.getRandomBean(Profile.sRandomFilePath);
        BaseHook.setRandomBean(bean);
        XposedBridge.hookAllConstructors(File.class, new FileHandler());
        XposedHelpers.setStaticObjectField(Build.class, "SERIAL", bean.getRandom_serial());
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", bean.getRandom_build_model());
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", bean.getRandom_os_version());
        HookManger.addHooks(new TelephoneHook());
        HookManger.addHooks(new RuntimeHook());
        HookManger.addHooks(new WifiHook());
        HookManger.addHooks(new SettingHook());
        HookManger.addHooks(new LocationMangerHook());
//        HookManger.addHooks(new SDCardStatuHook());

//        HookManger.addHooks(new FileOutputStreamHook());
        HookManger.addHooks(new OutputStreamHook());
        HookManger.addHooks(new InputStreamHook());
        HookManger.startHook(loadPackageParam.classLoader);

    }


    class FileHandler extends XC_MethodHook {
        public static final String filePath = "storage/emulated/0/uber_save_file";
        private FileWriter writer;

        public FileHandler() {
            try {
                writer = new FileWriter(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            try {
                File file = (File) param.thisObject;
                if (!file.isDirectory() && (!(file.getName().contains(".apk")) && (!(file.getName().contains(".so"))))) {
                    XposedBridge.log("----------<>---------------->>>> " + param.thisObject);
//                    writer.append(file.toString() + "\n");
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    writer.close();
                }
            }
            super.afterHookedMethod(param);
        }
    }


    @Override
    protected void finalize() throws Throwable {
        Log.d("TAG", "Wori class had destroyed......................");
        super.finalize();
    }

}
