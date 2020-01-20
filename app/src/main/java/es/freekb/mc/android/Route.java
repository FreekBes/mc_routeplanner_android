package es.freekb.mc.android;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Route {
    private JSONObject originalObject;
    private ObjectArrayList items = new ObjectArrayList(new ArrayList<String>(), new ArrayList<Object>());
    private ObjectArrayList lines = new ObjectArrayList(new ArrayList<String>(), new ArrayList<Object>());
    private RouteData routeData;
    private JSONObject walking;

    public Route(JSONObject routeJSON, Context ctx) {
        originalObject = routeJSON;

        try {
            JSONObject jsonItems = routeJSON.getJSONObject("items");
            Iterator<String> itemsIterator = jsonItems.keys();
            while (itemsIterator.hasNext()) {
                Item tempItem = new Item(jsonItems.getJSONObject(itemsIterator.next()));
                items.add(tempItem.getId(), tempItem);
            }

            JSONObject jsonLines = routeJSON.getJSONObject("line_data");
            Iterator<String> linesIterator = jsonLines.keys();
            while (linesIterator.hasNext()) {
                Line tempLine = new Line(jsonLines.getJSONObject(linesIterator.next()), ctx);
                lines.add(tempLine.getName(), tempLine);
            }

            if (!routeJSON.isNull("route")) {
                routeData = new RouteData(routeJSON.getJSONObject("route"));
            }
            else {
                routeData = null;
            }
            walking = routeJSON.getJSONObject("walking");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ObjectArrayList getItems() {
        return items;
    }

    public ObjectArrayList getLines() {
        return lines;
    }

    public RouteData getRouteData() {
        return routeData;
    }

    public JSONObject getWalking() {
        return walking;
    }

    public JSONObject toJSONObject() {
        return originalObject;
    }
}
