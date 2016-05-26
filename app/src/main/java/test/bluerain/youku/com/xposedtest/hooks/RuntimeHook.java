package test.bluerain.youku.com.xposedtest.hooks;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class RuntimeHook extends BaseHook {
    @Override
    protected String setClassName() {
        return Runtime.class.getName();
    }

    @Override
    protected void setHookItem(List<HookInfo> hookInfoList) {

        hookInfoList.add(new HookInfo("exec", String.class, new XC_MethodHook() {
            String input = null;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                input = (String) param.args[0];
                XposedBridge.log("before exec -->" + param.args[0]);
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!TextUtils.isEmpty(input) && input.contains("address")) {
                    param.setResult(new java.lang.Process() {
                        @Override
                        public void destroy() {

                        }

                        @Override
                        public int exitValue() {
                            return 0;
                        }

                        @Override
                        public InputStream getErrorStream() {
                            return null;
                        }

                        @Override
                        public InputStream getInputStream() {
                            String s = "a1:11:11:11:11:11";
                            return new ByteArrayInputStream(s.getBytes());
                        }

                        @Override
                        public OutputStream getOutputStream() {
                            return null;
                        }

                        @Override
                        public int waitFor() throws InterruptedException {
                            return 0;
                        }
                    });
                }
                super.afterHookedMethod(param);
            }
        }));
    }
}
