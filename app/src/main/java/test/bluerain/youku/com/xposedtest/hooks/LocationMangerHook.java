package test.bluerain.youku.com.xposedtest.hooks;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/1/27.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class LocationMangerHook extends BaseHook {
    @Override
    protected String setClassName() {
        return LocationManager.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("getLastKnownLocation", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getLastKnownLocation Method has been called");
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLongitude(108.11111);
                location.setLatitude(39.2222222);
                param.setResult(location);
                super.afterHookedMethod(param);
            }
        }));
        hookInfoList.add(new HookInfo("requestSingleUpdate", Criteria.class, LocationListener.class, Looper.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hooked the method of onLocationChanged");
                Object[] args = param.args;
//                for (Object object : args) {
//                    if (null==object){XposedBridge.log("卧槽，参数为空");}
//                    else {
//                        XposedBridge.log(object.toString());
//                    }
//                }
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLongitude(116.2317);
                location.setLatitude(39.5427);
                LocationListener listener = (LocationListener) args[1];
                listener.onLocationChanged(location);
                super.beforeHookedMethod(param);
            }
        }));

    }
}
