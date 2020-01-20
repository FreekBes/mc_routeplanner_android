package es.freekb.mc.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Worlds {
    private static ArrayList<World> worlds = new ArrayList<World>();
    private static ArrayList<String> worldIds = new ArrayList<String>();

    public class World {
        private String id;
        private String name;
        private String displayName;
        private String dataUrl;
        private Boolean mapSupported;
        private String metroMapUrl;

        public World(JSONObject world, String worldId) {
            try {
                id = worldId;
                name = world.getString("name");
                displayName = world.getString("displayName");
                dataUrl = world.getString("data");
                mapSupported = world.getBoolean("mapSupported");
                metroMapUrl = world.optString("metroMap");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDataUrl() {
            return dataUrl;
        }

        public boolean hasMapSupport() {
            return mapSupported;
        }

        public String getMetroMapUrl() {
            return metroMapUrl;
        }
    }

    private int getWorldIndex(String worldId) {
        return worldIds.indexOf(worldId);
    }

    public World getWorld(String worldId) {
        int x = getWorldIndex(worldId);
        if (x > -1) {
            return worlds.get(x);
        }
        else {
            return null;
        }
    }

    public World getWorld(int index) {
        return worlds.get(index);
    }

    public ArrayList<String> getWorldIDs() {
        return worldIds;
    }

    public int getAmountOfWorlds() {
        return worlds.size();
    }

    public boolean worldsInitialized() {
        return worlds.size() > 0;
    }

    public Worlds(final Context ctx) {
        String tag_string_req = "worldsgetter";
        String url = AppController.mainUrl + "worlds.json";

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject tempWorlds = new JSONObject(response);
                    Iterator<String> keys = tempWorlds.keys();
                    while (keys.hasNext()) {
                        String worldId = keys.next();
                        worlds.add(new World(tempWorlds.getJSONObject(worldId), worldId));
                        worldIds.add(worldId);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    AppController.unknownError(ctx);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppController.noInternetError(ctx);
            }
        }) {

        };

        AppController.addToRequestQueue(ctx, req, tag_string_req);
    }
}
