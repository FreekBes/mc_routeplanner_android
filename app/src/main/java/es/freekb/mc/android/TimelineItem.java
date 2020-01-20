package es.freekb.mc.android;

import android.content.Context;

public class TimelineItem {
    public static final int TYPE_STATION = 1;
    public static final int TYPE_STATION_TRANSFER = 2;
    public static final int TYPE_INSTRUCTION = 3;
    public static final int TYPE_WALK = 4;
    public static final int TYPE_EMPTY = 5;

    public static final int CONNECTION_TOP = 1;
    public static final int CONNECTION_MIDDLE = 2;
    public static final int CONNECTION_BOTTOM = 3;

    private int type;
    private int connectionType = CONNECTION_MIDDLE;
    private String leftOfLine;
    private String mainText;
    private String extraText;
    private int platformNumber = 0;
    private boolean displayed;

    public TimelineItem(int timelineType) {
        type = timelineType;
        if (type == 1) {
            displayed = false;
        }
        else {
            displayed = true;
        }
    }

    public void setConnection(int connection) {
        connectionType = connection;
    }

    public int getConnection() {
        return connectionType;
    }

    public void setLeftOfLine(String content) {
        leftOfLine = content;
    }

    public String getLeftOfLine() {
        return leftOfLine;
    }

    public void setText(String text) {
        mainText = text;
    }

    public String getText() {
        return mainText;
    }

    public void setExtraText(String text) {
        extraText = text;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setPlatformNumber(int number) {
        platformNumber = number;
    }

    public int getPlatformNumber() {
        return platformNumber;
    }

    public String getPlatformNumberAsString(Context ctx) {
        return ctx.getString(R.string.internal_platform_with_number, platformNumber);
    }

    public void setVisibility(boolean visible) {
        displayed = visible;
    }

    public boolean isVisible() {
        return displayed;
    }

    public int getType() {
        return type;
    }
}
