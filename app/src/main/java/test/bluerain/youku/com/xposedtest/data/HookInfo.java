package test.bluerain.youku.com.xposedtest.data;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class HookInfo {
    String mthodName;
    Object[] paramXcMethod;

    public HookInfo(String mthodName, Object... paramXcMethod) {
        this.mthodName = mthodName;
        this.paramXcMethod = paramXcMethod;
    }


    public String getMthodName() {
        return mthodName;
    }

    public void setMthodName(String mthodName) {
        this.mthodName = mthodName;
    }

    public Object[] getParamXcMethod() {
        return paramXcMethod;
    }

    public void setParamXcMethod(Object[] paramXcMethod) {
        this.paramXcMethod = paramXcMethod;
    }
}
