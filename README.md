# TinkerDemo
Android Tinker 热修复接入记录

Tinker 热修复
该工程主要记录接入Tinker的过程

1，项目build.gradle 添加tinker-patch-gradle-plugin依赖

    dependencies {
            classpath 'com.android.tools.build:gradle:3.2.0'
            //thinker
            classpath ('com.tencent.tinker:tinker-patch-gradle-plugin:1.9.1')
            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
    }

2，在app/build.gradle 添加Tinker依赖

    dependencies {
        implementation fileTree(dir: 'libs', include: ['*.jar'])
        implementation 'com.android.support:appcompat-v7:28.0.0'
        implementation 'com.android.support.constraint:constraint-layout:1.1.3'

        //butterknife
        implementation 'com.jakewharton:butterknife:8.4.0'
        annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'

        //thinker
        //可选，用于生成application类
        compileOnly ('com.tencent.tinker:tinker-android-anno:1.9.1')
        //tinker的核心库
        implementation ('com.tencent.tinker:tinker-android-lib:1.9.1')
        implementation "com.android.support:multidex:1.0.1"
    }

3，app/build.gradle 其他地方修改 (建议直接复制)

    配置签名

4，Application编写

    参照工程中的SimpleTinkerApplicationLike

5，加载/清除 补丁

    //path 指: 开发过程中约定的路径(Tinker demo中是sdcard中，需要添加sd卡权限)
    TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);

    Tinker.with(getApplicationContext()).cleanPatch();

6，修改app/build.gradle

    ext {
        //当需要生成补丁的时候设置成true
        tinkerEnabled = true

        //for normal build
        //old apk file to build patch apk
        //bug app路径
        tinkerOldApkPath = "${bakPath}/app-debug-0522-18-25-02.apk"

        //proguard mapping file to build patch apk
        tinkerApplyMappingPath = "${bakPath}/app-debug-1018-17-32-47-mapping.txt"
        //resource R.txt to build patch apk, must input if there is resource changed
        //资源文件修改
        tinkerApplyResourcePath = "${bakPath}/app-debug-0522-18-25-02-R.txt"

        //only use for build all flavor, if not, just ignore this field
        tinkerBuildFlavorDirectory = "${bakPath}/app-1018-17-32-47"
    }

    上述是指好之后
    Gradle -> tinker ->:app(测试中不选这个也行) ->Tasks -> tinker ->tinkerPatchDebug
    执行 生成补丁包

    补丁包路径
    app/build/outputs/apk/tinkerPath/debug/patch_signed_7zip.apk

7, 将该补丁包放到手机/模拟器 sdcard指定路径

8，点击load按钮加载补丁， 程序自动退出。 重新启动app，补丁即会生效

