package org.ar25.androidno.mvp;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import javax.inject.Inject;


public abstract class BaseFragment<P extends BasePresenter<V>, V extends MvpView> extends Fragment implements MvpView {

    @Inject P presenter;

    @Override public void onStart() {

        super.onStart();
        presenter.attach(getMvpView());
    }

    @Override public void onStop() {

        presenter.detach();
        super.onStop();
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

    protected abstract V getMvpView();

    protected P getPresenter() {
        return presenter;
    }
}
