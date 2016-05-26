package test.bluerain.youku.com.xposedtest.hooks;

import android.net.wifi.WifiInfo;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/2/19.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class WifiHook extends BaseHook {
    @Override
    protected String setClassName() {
        return WifiInfo.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getIpAddress", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook the ipAddress ");
                int org = (int) param.getResult();
                int wei = (int) (Math.random() * 10);
                int result = org >> wei;
                param.setResult(result);
                super.afterHookedMethod(param);
            }
        }));
        hookInfoList.add(new HookInfo("getMacAddress", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = randomBean.getRandom_mac_address();
                XposedBridge.log("hook the mac地址 ,已经设置为[" + result + "]");
                param.setResult(result);
                super.afterHookedMethod(param);
            }
        }));
    }
}
