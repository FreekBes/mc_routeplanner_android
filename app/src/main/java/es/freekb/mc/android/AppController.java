package es.freekb.mc.android;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    public static final String mainUrl = "https://freekb.es/routeplanner/";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static RequestQueue getRequestQueue(Context ctx) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx);
        }

        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Context ctx, Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(ctx).add(req);
    }

    public static <T> void addToRequestQueue(Context ctx, Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(ctx).add(req);
    }

    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void noInternetError(Context ctx) {
        new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.alert_no_internet_title))
                .setMessage(ctx.getString(R.string.alert_no_internet_content))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .show();
    }

    public static void unknownError(Context ctx) {
        new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.alert_unknown_error_title))
                .setMessage(ctx.getString(R.string.alert_unknown_error_content))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .show();
    }
}
