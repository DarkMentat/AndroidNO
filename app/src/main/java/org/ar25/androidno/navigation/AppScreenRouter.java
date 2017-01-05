package org.ar25.androidno.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import org.ar25.androidno.ApplicationContext;

import javax.inject.Inject;


public class AppScreenRouter implements ScreenRouter {

    private Context context;

    @Inject public AppScreenRouter(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override public Context getContext() {
        return context;
    }

    @Override public void finishScreen() {

    }

    @Override public void changeScreen(Intent intent) {
        context.startActivity(intent);
    }

    @Override public void changeScreen(Intent intent, View sharedElement) {

    }

    @Override public void changeScreen(Fragment fragment) {

    }

    @Override public void changeScreen(Fragment fragment, View sharedElement) {

    }

    @Override public void changeScreenWithResult(Intent intent, int requestCode) {

    }

    @Override public void changeScreenWithResult(Intent intent, View sharedElement, int requestCode) {

    }

    @Override public void openDialogWithResult(DialogFragment fragment, int requestCode) {

    }
}