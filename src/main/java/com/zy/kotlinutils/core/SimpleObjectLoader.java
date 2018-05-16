package com.zy.kotlinutils.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.Call;

/**
 * Created by zy on 17-5-26.
 */

public class SimpleObjectLoader {
    public interface Callback<T> {
        T load();
        void onLoaded(T obj);
    }

    public static <T> boolean load(final Callback<T> callback) {
        return load(callback, false);
    }

    public static <T> boolean load(final Callback<T> callback, boolean async) {
        if (callback == null) {
            return false;
        }
        Looper looper = Looper.myLooper();
        final Handler handler = looper == null ? async ? new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                T obj = msg.obj == null ? null : (T) msg.obj;
                callback.onLoaded(obj);
                return true;
            }
        }) : null : new Handler(looper, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                T obj = msg.obj == null ? null : (T) msg.obj;
                callback.onLoaded(obj);
                return true;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                T object;
                try {
                    object = callback.load();
                } catch (Exception e) {
                    object = null;
                }
                if (handler == null) {
                    callback.onLoaded(object);
                } else {
                    handler.obtainMessage(0, object).sendToTarget();
                }
            }
        };
        if (handler == null) {
            runnable.run();
        } else {
            new Thread(runnable).start();
        }
        return true;
    }
}
