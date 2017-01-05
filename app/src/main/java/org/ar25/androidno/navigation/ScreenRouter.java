package org.ar25.androidno.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Rosty on 10/19/2016.
 */

public interface ScreenRouter {

    Context getContext();

    void finishScreen();

    void changeScreen(Intent intent);

    void changeScreen(Intent intent, View sharedElement);

    void changeScreen(Fragment fragment);

    void changeScreen(Fragment fragment, View sharedElement);

    void changeScreenWithResult(Intent intent, int requestCode);

    void changeScreenWithResult(Intent intent, View sharedElement, int requestCode);

    void openDialogWithResult(DialogFragment fragment, int requestCode);
}
