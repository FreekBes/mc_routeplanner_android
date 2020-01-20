package es.freekb.mc.android;

import android.content.Context;

public class Warning {
    private String id;

    public Warning(String warningId) {
        id = warningId;
    }

    public String getId() {
        return id;
    }

    public String getWarningName(Context ctx) {
        switch (id) {
            case "single_track":
                return ctx.getString(R.string.route_warning_single_track);
            case "skeletons":
                return ctx.getString(R.string.route_warning_skeletons);
            case "zombies":
                return ctx.getString(R.string.route_warning_zombies);
            case "shared_platform":
                return ctx.getString(R.string.route_warning_shared_platform);
            case "mine_track":
                return ctx.getString(R.string.route_warning_mine_track);
            case "own_minecart":
                return ctx.getString(R.string.route_warning_own_minecart);
            case "cactus_breaker":
                return ctx.getString(R.string.route_warning_cactus_breaker);
            case "no_stop_take_off":
                return ctx.getString(R.string.route_warning_no_stop_take_off);
            case "no_fence":
                return ctx.getString(R.string.route_warning_no_fence);
            case "messy":
                return ctx.getString(R.string.route_warning_messy);
            case "left_side":
                return ctx.getString(R.string.route_warning_left_side);
            case "lever_for_minecart":
                return ctx.getString(R.string.route_warning_lever_for_minecart);
            default:
                return id;
        }
    }

    public String getWarningIcon() {
        switch (id) {
            case "single_track":
                return "icons/warnings/single-track.png";
            case "skeletons":
                return "icons/warnings/skeletons.png";
            case "zombies":
                return "icons/warnings/zombies.png";
            case "shared_platform":
                return "icons/warnings/shared-platform.png";
            case "mine_track":
                return "icons/warnings/mine-track.png";
            case "own_minecart":
                return "icons/warnings/own-minecart.png";
            case "no_stop_take_off":
                return "icons/warnings/no-stop-take-off.png";
            case "no_fence":
                return "icons/warnings/no-fence.png";
            case "messy":
                return "icons/warnings/messy.png";
            case "left_side":
                return "icons/warnings/left-side.png";
            case "lever_for_minecart":
                return "icons/warnings/lever-for-minecart.png";
            default:
                return "icons/warnings/no-icon.png";
        }
    }
}
