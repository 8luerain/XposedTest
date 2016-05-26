package test.bluerain.youku.com.xposedtest;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import test.bluerain.youku.com.xposedtest.data.HookInfo;
import test.bluerain.youku.com.xposedtest.hooks.BaseHook;
import test.bluerain.youku.com.xposedtest.hooks.Wori;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class HookManger {

    public static final String TAG = "Xposed";
    private static List<Wori> sWoris = new ArrayList<>();
    public  List<IHookPackage> hookListeners = new ArrayList<>();

    public static int count = 1;


    private static final HookManger INSTANCE = new HookManger();

    private HookManger() {

    }

    public static HookManger getInstance() {
        return INSTANCE;
    }


    public void addHook(IHookPackage hookListener) {
        if (!hookListeners.contains(hookListener)) {
            this.hookListeners.add(hookListener);
            Log.d(TAG, "add listener , now size is " + hookListeners.size());
        }
    }

    public void removeHook(IHookPackage hookListener) {
        if (hookListeners.contains(hookListener)) {
            hookListeners.remove(hookListener);
        }
    }

    public void hookPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("wohhhhhh----------------------------->" + loadPackageParam.packageName);
        Log.d(TAG, "on hook , listeners size is " + this.hookListeners.size());
        for (IHookPackage hookListener : this.hookListeners) {
            hookListener.onHookPackage(loadPackageParam);
        }
    }

    private static List<BaseHook> sBaseHooks = new ArrayList<>();

    public static List<BaseHook> getBaseHooks() {
        return sBaseHooks;
    }


    public static void addHooks(BaseHook hook) {
        sBaseHooks.add(hook);
    }

    public static void addWoris(Wori w) {
        sWoris.add(w);
        Log.d(TAG, "-----------------<>>>>> wo ri size is " + sWoris.size());
    }

    public static void startHook(ClassLoader loader) {
        for (BaseHook hook : sBaseHooks) {
            List<HookInfo> allHooks = hook.getAllHooks();
            for (HookInfo hookInfo : allHooks) {
                XposedHelpers.findAndHookMethod(hook.getClassName(), loader, hookInfo.getMthodName(), hookInfo.getParamXcMethod());
            }
        }
    }


    interface IHookPackage {
        void onHookPackage(XC_LoadPackage.LoadPackageParam loadPackageParam);
    }
}
