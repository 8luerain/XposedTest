package test.bluerain.youku.com.xposedtest.hooks;

import java.net.HttpURLConnection;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Created by rain on 2016/1/29.
 */
public class HttpUrlConnectionHook extends BaseHook {
    @Override
    protected String setClassName() {
        return HttpURLConnection.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getOutputStream", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        }));

    }
}
