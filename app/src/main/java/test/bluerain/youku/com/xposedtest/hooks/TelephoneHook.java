package test.bluerain.youku.com.xposedtest.hooks;

import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class TelephoneHook extends BaseHook {


    @Override
    protected String setClassName() {
        return TelephonyManager.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                XposedBridge.log("hook getDeviceId method , IMEI have been set");
                param.setResult(CommonUtils.getRandomNumByLine(0));
                super.afterHookedMethod(param);
            }
        }));

        hookInfoList.add(new HookInfo("getSubscriberId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook getSubscriberId method , IMSI have been set");
                param.setResult(CommonUtils.getRandomNumByLine(1));
                super.afterHookedMethod(param);
            }
        }));

        hookInfoList.add(new HookInfo("getSimSerialNumber", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook getSimSerialNumber method , SIM_Serial have been set");
                param.setResult(CommonUtils.getRandomNumByLine(4));
                super.afterHookedMethod(param);
            }
        }));
        hookInfoList.add(new HookInfo("getLine1Number", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook getLine1Number method , PhoneNum have been set");
                param.setResult(CommonUtils.getRandomNumByLine(5));
                super.afterHookedMethod(param);
            }
        }));

    }
}
