package test.bluerain.youku.com.xposedtest;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import test.bluerain.youku.com.xposedtest.data.HookInfo;
import test.bluerain.youku.com.xposedtest.hooks.BaseHook;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class HookManger {

    private static List<BaseHook> mBaseHooks = new ArrayList<>();

    public List<BaseHook> getBaseHooks() {
        return mBaseHooks;
    }

    public static void addHooks(BaseHook hook) {
        mBaseHooks.add(hook);
    }

    public static void startHook(ClassLoader loader) {
        for (BaseHook hook : mBaseHooks) {
            List<HookInfo> allHooks = hook.getAllHooks();
            for (HookInfo hookInfo : allHooks) {
                XposedHelpers.findAndHookMethod(hook.getClassName(), loader, hookInfo.getMthodName(), hookInfo.getParamXcMethod());
            }
        }
    }
}
