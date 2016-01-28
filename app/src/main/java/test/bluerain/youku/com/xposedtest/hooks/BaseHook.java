package test.bluerain.youku.com.xposedtest.hooks;

import java.util.ArrayList;
import java.util.List;

import test.bluerain.youku.com.xposedtest.data.HookInfo;

/**
 * Project: XposedTest.
 * Data: 2016/1/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public abstract class BaseHook {

    private List<HookInfo> mHookInfos = new ArrayList<>();

    private String mClassName;

    public BaseHook() {
        mClassName = setClassName();
        setHookItem(mHookInfos);
    }

    protected abstract String setClassName();


    public String getClassName() {
        return mClassName;
    }

    public void addHook(HookInfo info) {
        mHookInfos.add(info);
    }

    public void removeHook(HookInfo info) {
        if (mHookInfos.contains(info))
            mHookInfos.remove(info);
    }

    public List<HookInfo> getAllHooks() {
        return mHookInfos;
    }

    protected abstract void setHookItem(List<HookInfo> hookInfoList);
}
