package es.freekb.mc.android;

public class Planner {
    private String currentWorldId = null;
    private int currentWorldIndex = -1;
    private Item from = null;
    private Item to = null;

    public void setWorld(String worldId, int worldIndex) {
        currentWorldId = worldId;
        currentWorldIndex = worldIndex;
    }

    public String getCurrentWorldId() {
        return currentWorldId;
    }

    public int getCurrentWorldIndex() {
        return currentWorldIndex;
    }

    public Planner() {

    }

    public boolean fromEmpty() {
        return from == null;
    }

    public void setFrom(Item from) {
        this.from = from;
    }

    public Item getFrom() {
        return from;
    }

    public boolean toEmpty() {
        return to == null;
    }

    public void setTo(Item to) {
        this.to = to;
    }

    public Item getTo() {
        return to;
    }

    public void reverseFromAndTo() {
        Item tempTo = to;
        to = from;
        from = tempTo;
    }
}
