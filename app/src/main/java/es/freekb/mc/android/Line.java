package es.freekb.mc.android;

import android.content.Context;

import org.json.JSONObject;

public class Line {
    private JSONObject originalObject;
    private String type;
    private String name;
    private String operator;

    public Line(JSONObject line, Context ctx) {
        originalObject = line;

        type = line.optString("type", "connection");
        name = line.optString("line_name", "");
        operator = line.optString("operator", ctx.getString(R.string.internal_default_name_operator));
    }

    public String getType() {
        return type;
    }

    public String getTypeReadable(Context ctx) {
        switch (type) {
            case "train":
                return ctx.getString(R.string.internal_connection_type_train);
            case "metro":
            case "subway":
                return ctx.getString(R.string.internal_connection_type_subway);
            case "ring_line":
                return ctx.getString(R.string.internal_connection_type_ring_line);
            case "tram":
                return ctx.getString(R.string.internal_connection_type_tram);
            case "ferry":
                return ctx.getString(R.string.internal_connection_type_ferry);
            case "mine":
                return ctx.getString(R.string.internal_connection_type_mine);
            default:
                return ctx.getString(R.string.internal_connection_type_default);
        }
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public JSONObject toJSONObject() {
        return originalObject;
    }
}
