package es.freekb.mc.android;

import java.util.ArrayList;

public class ObjectArrayList {
    private ArrayList<String> keys;
    private ArrayList<Object> items;

    public ObjectArrayList(ArrayList<String> keys, ArrayList<Object> items) {
        this.keys = keys;
        this.items = items;
    }

    public int indexOf(String key) {
        return keys.indexOf(key);
    }

    public Object get(String key) {
        int index = this.indexOf(key);
        if (index > -1) {
            return items.get(index);
        }
        else {
            throw new IndexOutOfBoundsException("Key " + key + " not found in index (full index: " + keys.toString() + ")");
        }
    }

    public void add(String key, Object value) {
        keys.add(key);
        items.add(value);
    }

    public void remove(String key) {
        int index = this.indexOf(key);
        if (index > -1) {
            keys.remove(index);
            items.remove(index);
        }
        else {
            throw new IndexOutOfBoundsException("Key not found in index");
        }
    }

    public int size() {
        return keys.size();
    }

    public void clear() {
        keys.clear();
        items.clear();
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }
}
