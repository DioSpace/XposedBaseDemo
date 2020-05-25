package com.my.xposedbasedemo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Arrow implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // TODO Auto-generated method stub
        XposedBridge.log("Loaded app ==== Congratulations! hook is go! =====");
        // 不是需要 Hook 的包直接返回
        if (!loadPackageParam.packageName.equals("com.my.xposedtargetdemo"))
            return;

        XposedBridge.log("app包名：" + loadPackageParam.packageName);

        XposedHelpers.findAndHookMethod("com.my.xposedtargetdemo.Util", // 被Hook函数所在的类(包名+类名)
                loadPackageParam.classLoader, "ordinaryFunc", // 被Hook函数的名称ordinaryFunc
                String.class, // 被Hook函数的第一个参数String
                String.class, // 被Hook函数的第二个参数String
                int.class,// 被Hook函数的第三个参数integer
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        super.beforeHookedMethod(param);
                        // Hook函数之前执行的代码
                        // 打印方法的参数信息
                        XposedBridge.log("param1：" + param.args[0]);
                        XposedBridge.log("parma2：" + param.args[1]);
                        XposedBridge.log("parma3：" + param.args[2]);
                        // 打印堆栈查看调用关系
                        StackTraceElement[] mylogs = new Throwable("mylogs")
                                .getStackTrace();
                        for (int i = 0; i < mylogs.length; i++) {
                            XposedBridge.log("查看堆栈：" + mylogs[i].toString());
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        super.afterHookedMethod(param);
                        // Hook函数之后执行的代码
                        // 获取类
                        Class<?> clazz = param.thisObject.getClass();
                        XposedBridge.log("要hook的方法所在的类：" + clazz.getName());

                        // 获取方法的返回值
                        Object resultString = param.getResult();
                        XposedBridge.log("返回值：" + resultString.toString());
                        // 修改方法的返回值
                        param.setResult("Target was hooked!");
                    }
                });

    }

}
