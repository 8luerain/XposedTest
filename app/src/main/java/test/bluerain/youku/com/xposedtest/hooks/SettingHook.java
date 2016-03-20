package test.bluerain.youku.com.xposedtest.hooks;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import test.bluerain.youku.com.xposedtest.data.HookInfo;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;

/**
 * Project: XposedTest.
 * Data: 2016/2/2.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class SettingHook extends BaseHook {
    @Override
    protected String setClassName() {
        return Settings.Secure.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getString", ContentResolver.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("Xposed", "hook the method of android........");
                String s = (String) param.args[1];
                if (s.equals(Settings.Secure.ANDROID_ID)) {
                    param.setResult(getRandomBean().getRandom_android());
                }
                super.afterHookedMethod(param);
            }
        }));

    }
}
