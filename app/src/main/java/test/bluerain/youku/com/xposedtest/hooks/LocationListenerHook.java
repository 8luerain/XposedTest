package test.bluerain.youku.com.xposedtest.hooks;

import android.location.Location;
import android.location.LocationListener;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/1/27.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class LocationListenerHook extends BaseHook {
    @Override
    protected String setClassName() {
        return LocationListener.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("onLocationChanged", Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("onLocationChanged method has been called");
                super.beforeHookedMethod(param);
            }
        }));

    }
}
