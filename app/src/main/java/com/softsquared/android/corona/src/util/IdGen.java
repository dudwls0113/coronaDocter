package com.softsquared.android.corona.src.util;

import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGen {
    final static private AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    static private int _generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    static public int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return _generateViewId();
        } else {
            return View.generateViewId();
        }
    }
}