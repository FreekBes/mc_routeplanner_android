package es.freekb.mc.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Activity ctx;
    private ArrayList<Item> items;

    public ItemAdapter(Activity c, ArrayList<Item> items) {
        ctx = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public int getItemViewType(int index) {
        return 0;
    }

    public void updateItems(ArrayList<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public View getView(final int index, View convertView, ViewGroup parent) {
        Item item = items.get(index);
        if (convertView == null) {
            convertView = new View(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_item, parent, false);
        }

        // icon
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
        Picasso.with(ctx).load(item.getIconUri())
                .fit()
                .into(itemIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    public void onError() {

                    }
                });

        // name
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(item.getName());

        // details
        TextView itemDetails = (TextView) convertView.findViewById(R.id.itemDetails);
        String details = item.getTypeIconAndName()[1];
        if (item.hasLocation()) {
            details += " \u2022 " + item.getLocation();
        }
        itemDetails.setText(details);

        return convertView;
    }
}
