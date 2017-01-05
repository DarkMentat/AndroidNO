package org.ar25.androidno.mvp;


public interface MvpPresenter<V extends MvpView> {

    void attach(V mvpView);

    void detach();

    void destroy();

    void onRequestPermissionsResult(int requestCode);
}
