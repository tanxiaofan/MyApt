package com.github.tanxiaofan.api;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description: 绑定控件工具
 * @Author: fan.tan
 * @CreateDate: 2019/11/25 14:45
 */
public class MyBindView {

    /**
     * 在使用控件之前调用，完成控件绑定
     */
    public static void bind(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        String className = activity.getClass().getName() + "_ViewBinding";

        try {
            Class<?> bindingClass = activity.getClass().getClassLoader().loadClass(className);
            Constructor<?> constructor = bindingClass.getConstructor(activity.getClass(), View.class);
            constructor.newInstance(activity, decorView);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
