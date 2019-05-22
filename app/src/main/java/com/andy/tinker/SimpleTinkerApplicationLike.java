package com.andy.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@DefaultLifeCycle(application = ".TinkerInApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class SimpleTinkerApplicationLike extends DefaultApplicationLike {

    private static final String TAG = "TinkerApplicationLike";

    public SimpleTinkerApplicationLike(Application application, int tinkerFlags,
                                       boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                       long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        //初始化Tinker
        TinkerInstaller.install(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
        Log.d(TAG, "SimpleTinkerInApplicationLike的registerActivityLifecycleCallbacks方法执行了");
    }
}
