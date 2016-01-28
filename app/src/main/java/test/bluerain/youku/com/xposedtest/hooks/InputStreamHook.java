package test.bluerain.youku.com.xposedtest.hooks;

import java.io.InputStream;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Created by rain on 2016/1/29.
 */
public class InputStreamHook extends BaseHook {
    @Override
    protected String setClassName() {
        return InputStream.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("read", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook read method of inputstream");
                byte[] bytes = (byte[]) param.args[0];
                String s = new String(bytes);
                XposedBridge.log("read method is ---------------------> \n" + s);
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        }));

    }
}
