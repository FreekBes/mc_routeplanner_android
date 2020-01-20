package es.freekb.mc.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Route {
    private JSONObject originalObject;
    private ArrayList<Item> items;

    public Route(JSONObject route) {
        originalObject = route;

        try {
            JSONArray jsonItems = route.getJSONArray("items");
            for (int i = 0; i < jsonItems.length(); i++) {
                items.add(new Item(jsonItems.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        return originalObject;
    }
}
