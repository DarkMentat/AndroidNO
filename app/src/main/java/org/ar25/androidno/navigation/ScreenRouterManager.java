package org.ar25.androidno.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import org.ar25.androidno.ApplicationContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;


public class ScreenRouterManager {

    private PublishSubject<ActivityResult> activityResults  = PublishSubject.create();
    private PublishSubject<ActionResult> actionResults    = PublishSubject.create();
    private int                            requestCodeCount = 100;

    private Context context;
    private ScreenRouter router;
    private ScreenRouter appRouter;

    @Inject public ScreenRouterManager(@ApplicationContext Context context) {
        this.context = context;
        this.appRouter = new AppScreenRouter(context);
        this.router = appRouter;
    }

    /**
     * Setting current routing view
     *
     * @param router {@link org.ar25.androidno.mvp.MvpView} that implement {@link ScreenRouter}
     */
    public void setRouter(ScreenRouter router) {
        this.router = router;
    }

    /**
     * Removing current routing view. Automatically set {@link #appRouter} as current
     */
    public void removeRouter(ScreenRouter router) {
        if (this.router.equals(router)) this.router = appRouter;
    }

    /**
     * Open activity from intent without finishing current one
     *
     * @param activityBuilder with activity class & args
     */
    public void openScreen(ActivityBuilder activityBuilder) {
        openScreen(activityBuilder, false);
    }

    public void openScreen(Intent intent) {
        openScreen(intent, false);
    }
    public void openScreen(Intent intent, boolean finishCurrent) {

        router.changeScreen(intent);
        if (finishCurrent) router.finishScreen();
    }

    /**
     * Open activity with shared element from intent without finishing current one
     *
     * @param activityBuilder with activity class & args
     * @param sharedElement   between current activity and next one
     */
    public void openScreen(ActivityBuilder activityBuilder, View sharedElement) {
        openScreen(activityBuilder, sharedElement, false);
    }

    /**
     * Open activity from intent
     *
     * @param activityBuilder with activity class & args
     * @param finishCurrent   true - finish current, false - don't finish
     */
    public void openScreen(ActivityBuilder activityBuilder, boolean finishCurrent) {

        router.changeScreen(activityBuilder.build(context));
        if (finishCurrent) router.finishScreen();
    }


    /**
     * Open activity with shared element from intent
     *
     * @param activityBuilder with activity class & args
     * @param sharedElement   between current activity and next one
     * @param finishCurrent   true - finish current, false - don't finish
     */
    public void openScreen(ActivityBuilder activityBuilder, View sharedElement, boolean finishCurrent) {

        Intent intent = activityBuilder.build(context);

        router.changeScreen(intent, sharedElement);
        if (finishCurrent) router.finishScreen();
    }

    /**
     * Replace or show fragment with shared element
     *
     * @param fragment Fragment that need to be shown
     */
    public void openScreen(Fragment fragment) {
        router.changeScreen(fragment);
    }

    /**
     * Replace or show fragment
     *
     * @param fragment      Fragment that need to be shown
     * @param sharedElement between current fragment and next one
     */
    public void openScreen(Fragment fragment, View sharedElement) {
        router.changeScreen(fragment, sharedElement);
    }

    /**
     * Open new Activity that have to return some result
     *
     * @param intent for activity
     * @return observable for activity result
     */
    public Observable<ActivityResult> openScreenWithResult(Intent intent) {
        return openScreenWithResult(intent, null);
    }

    /**
     * Open new Activity that have to return some result with shared element
     *
     * @param intent        for activity
     * @param sharedElement between current activity and next one
     * @return observable for activity result
     */
    public Observable<ActivityResult> openScreenWithResult(Intent intent, View sharedElement) {

        int requestCode = getRequestCode();

        return activityResults
                .filter(result -> result.getRequestCode() == requestCode)
                .switchMap(result -> {
                    if (result.getResultCode() == Activity.RESULT_CANCELED) {

                        return Observable.error(new BadResultCodeException(
                                result.getRequestCode(),
                                result.getData()));

                    } else return Observable.just(result);
                })
                .take(1)
                .doOnSubscribe(() -> {
                    if (sharedElement == null) router.changeScreenWithResult(intent, requestCode);
                    else router.changeScreenWithResult(intent, sharedElement, requestCode);
                });
    }

    /**
     * Open new DialogFragment that have to return some result
     *
     * @param dialog instance of dialog fragment
     * @return observable for dialog result
     */
    @SuppressWarnings ("unchecked")
    public <T> Observable<ActionResult<T>> openDialogWithResult(DialogFragment dialog) {

        int requestCode = getRequestCode();

        return actionResults
                .filter(result -> result.getRequestCode() == requestCode)
                .switchMap(result -> Observable.just((ActionResult<T>) result))
                .take(1)
                .doOnSubscribe(() -> router.openDialogWithResult(dialog, requestCode));
    }

    /**
     * Setting {@link #requestCodeCount} after calling {@link #openDialogWithResult(DialogFragment)}
     */
    public <T> void actionResult(int requestCode, T data) {

        ActionResult<T> result = new ActionResult<>(data, requestCode);

        actionResults.onNext(result);
    }

    /**
     * Setting {@link #requestCodeCount} after calling {@link #openScreenWithResult(Intent)} or
     * {@link #openScreenWithResult(Intent, View)}
     */
    public void activityResult(int requestCode, int resultCode, Intent data) {

        ActivityResult result = new ActivityResult(requestCode, resultCode, data);

        activityResults.onNext(result);
    }

    private int getRequestCode() {
        return requestCodeCount++;
    }

    public class ActivityResult {

        private int    requestCode;
        private int    resultCode;
        private Intent data;

        public ActivityResult(int requestCode, int resultCode, Intent data) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public int getResultCode() {
            return resultCode;
        }

        public Intent getData() {
            return data;
        }
    }

    public class ActionResult<T> {

        private T   result;
        private int requestCode;

        public ActionResult(T result, int requestCode) {
            this.result = result;
            this.requestCode = requestCode;
        }

        public T getResult() {
            return result;
        }

        public int getRequestCode() {
            return requestCode;
        }
    }

    public class BadResultCodeException extends Exception {

        private int    requestCode;
        private Intent data;

        public BadResultCodeException(int requestCode, Intent data) {
            this.requestCode = requestCode;
            this.data = data;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public Intent getData() {
            return data;
        }
    }

    public static class ActivityBuilder {

        private Class  clazz;
        private Map<String, Object> args;

        public ActivityBuilder(Class clazz) {
            this.clazz = clazz;
            this.args = new HashMap<>();
        }

        public void putArg(String key, Object value) {
            args.put(key, value);
        }

        private Intent build(Context context){

            Intent intent = new Intent(context, clazz);
            intent.putExtras(mapToBundle(args));

            return intent;
        }
    }

    public static class FragmentBuilder {

        private Fragment fragment;
        private Map<String, Object> args;

        public FragmentBuilder(Fragment fragment) {
            this.fragment = fragment;
            this.args = new HashMap<>();
        }

        public void putArg(String key, Object value) {
            args.put(key, value);
        }

        private Fragment build(){

            fragment.setArguments(mapToBundle(args));

            return fragment;
        }
    }

    private static Bundle mapToBundle(Map<String, Object> map){

        Bundle bundle = new Bundle();

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            Object value = entry.getValue();

            if(value instanceof Boolean) bundle.putBoolean(entry.getKey(), (Boolean) value);

            if(value instanceof Short) bundle.putShort(entry.getKey(), (Short) value);
            if(value instanceof Integer) bundle.putInt(entry.getKey(), (Integer) value);
            if(value instanceof Long) bundle.putLong(entry.getKey(), (Long) value);

            if(value instanceof Float) bundle.putFloat(entry.getKey(), (Float) value);
            if(value instanceof Double) bundle.putDouble(entry.getKey(), (Double) value);

            if(value instanceof Character) bundle.putChar(entry.getKey(), (Character) value);
            if(value instanceof CharSequence) bundle.putCharSequence(entry.getKey(), (CharSequence) value); // Short[] -> short[]
            if(value instanceof String) bundle.putString(entry.getKey(), (String) value);

            if(value instanceof Serializable) bundle.putSerializable(entry.getKey(), (Serializable) value);
            if(value instanceof Parcelable) bundle.putParcelable(entry.getKey(), (Parcelable) value);
            if(value instanceof Bundle) bundle.putBundle(entry.getKey(), (Bundle) value);

            if(value instanceof Parcelable[]) bundle.putParcelableArray(entry.getKey(), (Parcelable[]) value);
            if(value instanceof boolean[]) bundle.putBooleanArray(entry.getKey(), (boolean[]) value);
            if(value instanceof short[]) bundle.putShortArray(entry.getKey(), (short[]) value);
            if(value instanceof int[]) bundle.putIntArray(entry.getKey(), (int[]) value);
            if(value instanceof long[]) bundle.putLongArray(entry.getKey(), (long[]) value);
            if(value instanceof float[]) bundle.putFloatArray(entry.getKey(), (float[]) value);
            if(value instanceof double[]) bundle.putDoubleArray(entry.getKey(), (double[]) value);
            if(value instanceof char[]) bundle.putCharArray(entry.getKey(), (char[]) value);
            if(value instanceof CharSequence[]) bundle.putCharSequenceArray(entry.getKey(), (CharSequence[]) value);
            if(value instanceof String[]) bundle.putStringArray(entry.getKey(), (String[]) value);
        }

        return bundle;
    }
}
