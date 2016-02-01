package test.bluerain.youku.com.xposedtest.hooks;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Created by rain on 2016/1/29.
 */
public class OutputStreamHook extends BaseHook {
    @Override
    protected String setClassName() {
        return OutputStream.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {
        hookInfoList.add(new HookInfo("write", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook the write method of outputStream class ");
                byte[] dataByte =(byte[]) param.args[0];
                String s = new String(dataByte);
                XposedBridge.log("output---------------------------\n " + s);
                super.beforeHookedMethod(param);

            }
        }));

    }
}
