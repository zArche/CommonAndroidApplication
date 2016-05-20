package name.arche.commonandroidapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

import name.arche.commonandroidapplication.ui.activity.MainActivity;

public class ActivityUtils {
    private static Stack<Activity> activityStack;

    public static <T> void startActivity(Activity activity, Class<T> clazz, boolean killOlder, Bundle params, int requestCode, int flags) {
        //
        if (killOlder) activity.finish();
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(params);
        // Flags
        if (flags != -1) {
            intent.addFlags(flags);
        }
        //
        if (requestCode != -1) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, boolean killOlder, Bundle params) {
        startActivity(activity, clazz, killOlder, params, -1, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, Bundle params, int requestCode) {
        startActivity(activity, clazz, false, params, requestCode, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, Bundle params) {
        startActivity(activity, clazz, false, params, -1, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, boolean killOlder, HashMap<String, Object> params, int requestCode, int flags) {
        Bundle bundle = new Bundle();
        for (String name : params.keySet()) {
            Object val = params.get(name);
            if (val instanceof String) {
                bundle.putString(name, (String) val);
            } else if (val instanceof Integer) {
                bundle.putInt(name, (Integer) val);
            } else if (val instanceof Long) {
                bundle.putLong(name, (Long) val);
            } else if (val instanceof Short) {
                bundle.putShort(name, (Short) val);
            } else if (val instanceof Float) {
                bundle.putFloat(name, (Float) val);
            } else if (val instanceof Double) {
                bundle.putDouble(name, (Double) val);
            } else {
                bundle.putSerializable(name, (Serializable) val);
            }
        }
        startActivity(activity, clazz, killOlder, bundle, requestCode, flags);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, boolean killOlder, HashMap<String, Object> params) {
        startActivity(activity, clazz, killOlder, params, -1, -1);
    }

    public static <T> void startActivity(Activity activity, Class<T> clazz, HashMap<String, Object> params, int requestCode) {
        startActivity(activity, clazz, false, params, requestCode, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, HashMap<String, Object> params) {
        startActivity(activity, clazz, false, params, -1, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, boolean killOlder) {
        startActivity(activity, clazz, killOlder, new HashMap<String, Object>(), -1, -1);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz, int flags) {
        startActivity(activity, clazz, false, new HashMap<String, Object>(), -1, flags);
    }


    public static <T> void startActivity(Activity activity, Class<T> clazz) {
        startActivity(activity, clazz, false, new HashMap<String, Object>(), -1, -1);
    }


    public static <T> void goHome(Activity activity, Class<T> clazz) {
        int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP;
        startActivity(activity, clazz, true, new HashMap<String, Object>(), -1, flags);
    }

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public static void appExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void backHomeActivity() {
        backHomeActivity(-1);
    }

    /**
     * 返回主页，并结束所以其他activity
     * */
    public static void backHomeActivity(int tabId) {
        try {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                if (null != activityStack.get(i)) {
                    if (activityStack.get(i).getClass() != MainActivity.class) {
                        finishActivity(activityStack.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}