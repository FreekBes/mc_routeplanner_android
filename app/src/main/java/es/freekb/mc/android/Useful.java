package es.freekb.mc.android;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

public class Useful {
    public static long getDistance(int[] from, int[] to) {
        int a = from[0] - to[0];
        int b = from[2] - to[2];
        return Math.round(Math.sqrt(a * a + b * b));
    }

    public static long getTimeOfArrival(int secondsToAddToCurrentTime) {
        return System.currentTimeMillis() + secondsToAddToCurrentTime * 1000;
    }

    public static String formatTime(Context ctx, long timestamp) {
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(ctx);
        Date time = new Date();
        time.setTime(timestamp);
        return timeFormat.format(time);
    }

    public static int[] jsonArrayToIntArray(JSONArray jsonArray) {
        int[] intArray = new int[jsonArray.length()];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = jsonArray.optInt(i);
        }
        return intArray;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        WindowManager wm = (WindowManager) listView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int deviceWidth;
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}