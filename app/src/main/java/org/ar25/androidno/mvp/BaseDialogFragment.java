package org.ar25.androidno.mvp;

import android.support.v4.app.DialogFragment;

import org.ar25.androidno.navigation.ScreenRouterManager;

import javax.inject.Inject;


public abstract class BaseDialogFragment extends DialogFragment implements MvpView {

    public static final String KEY_REQUEST_CODE = "key_request_code";

    @Inject ScreenRouterManager screenRouterManager;

    protected <D> void setResult(D result) {
        screenRouterManager.actionResult(getArguments().getInt(KEY_REQUEST_CODE, -1), result);
    }
}