package org.ar25.androidno.mvp;

import android.content.Context;


public interface MvpView {

    Context getContext();

    void requestPermission(String[] missingPermission, int requestCode);
}
