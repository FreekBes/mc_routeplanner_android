package es.freekb.mc.android;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Planner {
    private String currentWorldId = null;
    private int currentWorldIndex = -1;
    private Item from = null;
    private Item to = null;

    public interface PlannerListener {
        void routeCalculated(Route route);
        void routeCalculateError(String errorMessage);
        void readyStateChanged(boolean ready);
    }
    private PlannerListener plannerListener;

    public void setWorld(String worldId, int worldIndex) {
        currentWorldId = worldId;
        currentWorldIndex = worldIndex;
        from = null;
        to = null;
    }

    public String getCurrentWorldId() {
        return currentWorldId;
    }

    public int getCurrentWorldIndex() {
        return currentWorldIndex;
    }

    public Planner(PlannerListener plannerListener) {
        this.plannerListener = plannerListener;
    }

    public boolean fromEmpty() {
        return from == null;
    }

    public void setFrom(Item from) {
        this.from = from;
        if (readyToPlan()) {
            plannerListener.readyStateChanged(true);
        }
        else {
            plannerListener.readyStateChanged(false);
        }
    }

    public Item getFrom() {
        return from;
    }

    public boolean toEmpty() {
        return to == null;
    }

    public void setTo(Item to) {
        this.to = to;
        if (readyToPlan()) {
            plannerListener.readyStateChanged(true);
        }
        else {
            plannerListener.readyStateChanged(false);
        }
    }

    public Item getTo() {
        return to;
    }

    public void reverseFromAndTo() {
        Item tempTo = to;
        to = from;
        from = tempTo;
        if (readyToPlan()) {
            plannerListener.readyStateChanged(true);
        }
        else {
            plannerListener.readyStateChanged(false);
        }
    }

    public @Nullable Intent getPlannerIntent(Context ctx) {
        if (readyToPlan()) {
            Intent plannerIntent = new Intent(ctx, RouteActivity.class);
            plannerIntent.putExtra("world", this.getCurrentWorldId());
            plannerIntent.putExtra("worldIndex", this.getCurrentWorldIndex());
            plannerIntent.putExtra("from", this.getFrom().toJSONObject().toString());
            plannerIntent.putExtra("to", this.getTo().toJSONObject().toString());
            return plannerIntent;
        }
        else {
            return null;
        }
    }

    public String getUrl() {
        return AppController.mainUrl + "?f=" + from.getId() + "&t=" + to.getId() + "&w=" + currentWorldId;
    }

    public String getApiUrl() {
        return AppController.mainUrl + "api/planner.php?from=" + from.getId() + "&to=" + to.getId() + "&w=" + currentWorldId;
    }

    public boolean readyToPlan() {
        return !fromEmpty() && !toEmpty();
    }

    public void cancelPlanning() {
        AppController.cancelPendingRequests("planner");
    }

    public void plan(final Context ctx) {
        String tag_string_req = "planner";
        String url = getApiUrl();

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    if (responseJSON.getString("type").equals("success")) {
                        plannerListener.routeCalculated(new Route(responseJSON.getJSONObject("data"), ctx));
                    }
                    else {
                        plannerListener.routeCalculateError(responseJSON.optString("message", ctx.getString(R.string.alert_unknown_error_content)));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    plannerListener.routeCalculateError("JSON parse error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                plannerListener.routeCalculateError(ctx.getString(R.string.alert_no_internet_content));
            }
        }) {

        };

        AppController.addToRequestQueue(ctx, req, tag_string_req);
    }
}
