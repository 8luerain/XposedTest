package test.bluerain.youku.com.xposedtest.hooks;

import android.os.Environment;

import java.io.File;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class EnvironmentHook extends BaseHook {
    @Override
    protected String setClassName() {
        return Environment.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getExternalStorageDirectory", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("method of getExternalStorageDirectory hooked...");
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                File cheatFileDir = new File(Profile.DIDI_CHEAT_SD_DIR);
                if (!cheatFileDir.exists())
                    cheatFileDir.mkdir();
                param.setResult(cheatFileDir);
                XposedBridge.log("method of getExternalStorageDirectory return [" + ((File) param.getResult()).getAbsolutePath() + "]");
                super.afterHookedMethod(param);
            }
        }));

    }
}
