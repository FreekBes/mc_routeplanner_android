package es.freekb.mc.android;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    private JSONObject originalObject;
    private String id;
    private String type;
    private String subtype;
    private String name;
    private String location;
    private String halt;
    private int[] coords;

    public Item(JSONObject item) {
        try {
            originalObject = item;
            id = item.getString("id");
            type = item.getString("type");
            subtype = item.optString("subtype");
            name = item.getString("name");
            location = item.optString("location");
            halt = item.optString("halt");
            coords = Useful.jsonArrayToIntArray(item.getJSONArray("coords"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public boolean hasLocation() {
        return location != null && !location.equals("null");
    }

    public String getLocation() {
        return location;
    }

    public String getNearestHalt() {
        return halt;
    }

    public int[] getCoords() {
        return coords;
    }

    public String[] getTypeIconAndName() {
        String[] toReturn = new String[2];

        if (type == "station") {
            toReturn[0] = "icons/station.png";
            toReturn[1] = "Station";
        }
        else {
            switch (subtype) {
                case "station":
                    toReturn[0] = "icons/station.png";
                    toReturn[1] = "Station";
                    break;
                case "spawn":
                    toReturn[0] = "icons/place.png";
                    toReturn[1] = "Spawn";
                    break;
                case "end_portal":
                    toReturn[0] = "icons/portal.png";
                    toReturn[1] = "End Portal";
                    break;
                case "nether_portal":
                    toReturn[0] = "icons/portal.png";
                    toReturn[1] = "Nether Portal";
                    break;
                case "farm":
                    toReturn[0] = "icons/farm.png";
                    toReturn[1] = "Farm";
                    break;
                case "community_building":
                    toReturn[0] = "icons/bed.png";
                    toReturn[1] = "Communityhuis";
                    break;
                case "hotel":
                    toReturn[0] = "icons/bed.png";
                    toReturn[1] = "Hotel";
                    break;
                case "bank":
                    toReturn[0] = "icons/bank.png";
                    toReturn[1] = "Bank";
                    break;
                case "home":
                    toReturn[0] = "icons/home.png";
                    toReturn[1] = "Huis";
                    break;
                case "castle":
                    toReturn[0] = "icons/castle.png";
                    toReturn[1] = "Kasteel";
                    break;
                case "gate":
                    toReturn[0] = "icons/gate.png";
                    toReturn[1] = "Poort";
                    break;
                case "church":
                    toReturn[0] = "icons/church.png";
                    toReturn[1] = "Kerk";
                    break;
                case "stable":
                    toReturn[0] = "icons/parking.png";
                    toReturn[1] = "Paardenstal";
                    break;
                case "shop":
                    toReturn[0] = "icons/shop.png";
                    toReturn[1] = "Winkel";
                    break;
                case "food":
                    toReturn[0] = "icons/food.png";
                    toReturn[1] = "Eten";
                    break;
                case "viewpoint":
                    toReturn[0] = "icons/viewpoint.png";
                    toReturn[1] = "Uitzichtpunt";
                    break;
                case "terrain":
                    toReturn[0] = "icons/terrain.png";
                    toReturn[1] = "Landschap";
                    break;
                case "mine":
                    toReturn[0] = "icons/mine.png";
                    toReturn[1] = "Mijn";
                    break;
                case "art":
                    toReturn[0] = "icons/place.png";
                    toReturn[1] = "Kunstwerk";
                    break;
                case "enchanting_table":
                    toReturn[0] = "icons/place.png";
                    toReturn[1] = "Enchanting Table";
                    break;
                case "coords":
                    toReturn[0] = "icons/place.png";
                    toReturn[1] = "Coördinaten";
                    break;
                default:
                    toReturn[0] = "icons/place.png";
                    toReturn[1] = "Overig";
                    break;
            }
        }

        return toReturn;
    }

    public Uri getIconUri() {
        return Uri.parse(AppController.mainUrl + getTypeIconAndName()[0]);
    }

    public JSONObject toJSONObject() {
        return originalObject;
    }
}
