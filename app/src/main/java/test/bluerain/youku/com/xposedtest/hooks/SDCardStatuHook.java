package test.bluerain.youku.com.xposedtest.hooks;

import android.os.Environment;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/2/1.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class SDCardStatuHook extends BaseHook {
    @Override
    protected String setClassName() {
        return Environment.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getExternalStorageState", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook the method of getExternalStorageState");
                param.setResult(Environment.MEDIA_MOUNTED_READ_ONLY);
                super.afterHookedMethod(param);
            }
        }));

    }
}
