package org.ar25.androidno.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.ar25.androidno.navigation.ScreenRouter;
import org.ar25.androidno.navigation.ScreenRouterManager;

import javax.inject.Inject;


public abstract class BaseActivity<P extends BasePresenter<V>, V extends MvpView> extends AppCompatActivity implements MvpView, ScreenRouter {

    @Inject
    ScreenRouterManager screenRouterManager;
    @Inject P                   presenter;


    @Override public void onStart() {

        super.onStart();
        screenRouterManager.setRouter(this);
        presenter.setIntent(getIntent());
        presenter.attach(getMvpView());
    }

    @Override protected void onResume() {

        super.onResume();
        screenRouterManager.setRouter(this);
    }

    @Override public void onStop() {

        presenter.detach();
        screenRouterManager.removeRouter(this);
        super.onStop();
    }

    @Override public Context getContext() {
        return this;
    }

    @Override public void requestPermission(String[] missingPermission, int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(missingPermission, requestCode);
    }

    @Override public void onRequestPermissionsResult(int requestCode,
                                                     @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode);
    }

    @Override public void finishScreen() {
        finish();
    }

    @Override public void changeScreen(Intent intent) {
        startActivity(intent);
    }

    @Override public void changeScreen(Intent intent, View sharedElement) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            Pair<View, String>[] sharedViews = ViewUtils.parseSharedView(sharedElement, this);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedViews);
//            startActivity(intent, options.toBundle());
//
//        } else

        startActivity(intent);
    }

    @Override public void changeScreen(Fragment fragment) {

    }

    @Override public void changeScreen(Fragment fragment, View sharedElement) {

    }

    @Override public void changeScreenWithResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override public void changeScreenWithResult(Intent intent, View sharedElement, int requestCode) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            Pair<View, String>[] sharedViews = ViewUtils.parseSharedView(sharedElement, this);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedViews);
//
//            startActivityForResult(intent, requestCode, options.toBundle());
//
//        } else

        startActivityForResult(intent, requestCode);
    }

    @Override public void openDialogWithResult(DialogFragment fragment, int requestCode) {

        if (fragment.getArguments() == null) {
            fragment.setArguments(new Bundle());
        }

        fragment.getArguments().putInt(BaseDialogFragment.KEY_REQUEST_CODE, requestCode);

        fragment.show(getSupportFragmentManager(), String.valueOf(requestCode));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        screenRouterManager.activityResult(requestCode, resultCode, data);
    }

    protected abstract V getMvpView();

    protected P getPresenter() {
        return presenter;
    }
}
