package es.freekb.mc.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteData {
    private JSONObject originalJSON;
    private int distance;
    private ArrayList<String> lines = new ArrayList<String>();
    private ArrayList<Integer> durations = new ArrayList<Integer>();
    private ArrayList<ArrayList<Warning>> warnings = new ArrayList<ArrayList<Warning>>();
    private ArrayList<Integer> platforms = new ArrayList<Integer>();
    private ArrayList<String> halts = new ArrayList<String>();

    public RouteData(JSONObject routeData) {
        originalJSON = routeData;
        try {
            distance = routeData.optInt("distance", 0);
            JSONArray linesJSON = routeData.getJSONArray("lines");
            for (int i = 0; i < linesJSON.length(); i++) {
                lines.add(linesJSON.optString(i, ""));
            }

            JSONArray durationsJSON = routeData.getJSONArray("durations");
            for (int i = 0; i < durationsJSON.length(); i++) {
                durations.add(durationsJSON.optInt(i, 0));
            }

            JSONArray warningsJSON = routeData.getJSONArray("warnings");
            for (int i = 0; i < warningsJSON.length(); i++) {
                warnings.add(new ArrayList<Warning>());
                JSONArray specificRouteWarningsJSON = warningsJSON.getJSONArray(i);
                for (int j = 0; j < specificRouteWarningsJSON.length(); j++) {
                    warnings.get(i).add(new Warning(specificRouteWarningsJSON.getString(j)));
                }
            }

            JSONArray platformsJSON = routeData.getJSONArray("platforms");
            for (int i = 0; i < platformsJSON.length(); i++) {
                platforms.add(platformsJSON.optInt(i, 0));
            }

            JSONArray haltsJSON = routeData.getJSONArray("halts");
            for (int i = 0; i < haltsJSON.length(); i++) {
                halts.add(haltsJSON.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDistance() {
        return distance;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public ArrayList<Integer> getDurations() {
        return durations;
    }

    public ArrayList<ArrayList<Warning>> getWarnings() {
        return warnings;
    }

    public ArrayList<Warning> getWarningsForRoutePart(int routePart) {
        return warnings.get(routePart);
    }

    public ArrayList<Integer> getPlatforms() {
        return platforms;
    }

    public ArrayList<String> getHalts() {
        return halts;
    }

    public JSONObject toJSONObject() {
        return originalJSON;
    }
}
