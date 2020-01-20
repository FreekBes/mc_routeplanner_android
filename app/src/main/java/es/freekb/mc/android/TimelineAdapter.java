package es.freekb.mc.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TimelineAdapter extends BaseAdapter {
    private Activity ctx;
    private ArrayList<TimelineItem> items;
    boolean expanded = false;

    public TimelineAdapter(Activity c, ArrayList<TimelineItem> items) {
        ctx = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public TimelineItem getItem(int index) {
        return items.get(index);
    }

    public void setExpanded(boolean doExpand) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setVisibility(doExpand);
        }
        expanded = doExpand;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public int getItemViewType(int index) {
        return items.get(index).getType();
    }

    public View getView(final int index, View convertView, ViewGroup parent) {
        TimelineItem item = items.get(index);
        int itemViewType = getItemViewType(index);
        if (convertView == null || true) {
            convertView = new View(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layoutIndex = 0;
            switch (itemViewType) {
                case TimelineItem.TYPE_STATION:
                    layoutIndex = R.layout.timeline_layout_default;
                    break;
                case TimelineItem.TYPE_STATION_TRANSFER:
                    switch (item.getConnection()) {
                        case TimelineItem.CONNECTION_MIDDLE:
                        default:
                            layoutIndex = R.layout.timeline_layout_transfer;
                            break;
                        case TimelineItem.CONNECTION_TOP:
                            layoutIndex = R.layout.timeline_layout_transfer_top;
                            break;
                        case TimelineItem.CONNECTION_BOTTOM:
                            layoutIndex = R.layout.timeline_layout_transfer_bottom;
                            break;
                    }
                    break;
                case TimelineItem.TYPE_INSTRUCTION:
                    layoutIndex = R.layout.timeline_layout_instruction;
                    break;
                case TimelineItem.TYPE_WALK:
                    layoutIndex = R.layout.timeline_layout_walk;
                    break;
                case TimelineItem.TYPE_EMPTY:
                    layoutIndex = R.layout.timeline_layout_empty;
                    break;
                default:
                    throw new IllegalStateException("TimelineItem type " + String.valueOf(getItemViewType(index)) + " does not exist");
            }
            convertView = inflater.inflate(layoutIndex, parent, false);
        }

        if (itemViewType == TimelineItem.TYPE_STATION || itemViewType == TimelineItem.TYPE_STATION_TRANSFER) {
            TextView textTime = (TextView) convertView.findViewById(R.id.textTime);
            textTime.setText(item.getLeftOfLine());

            TextView textMain = (TextView) convertView.findViewById(R.id.textMain);
            textMain.setText(item.getText());

            TextView textPlatform = (TextView) convertView.findViewById(R.id.textPlatform);
            if (item.getPlatformNumber() > 0) {
                textPlatform.setText(item.getPlatformNumberAsString(ctx).toLowerCase());
            }
            else if (item.getPlatformNumber() == 0) {
                textPlatform.setText("- - - - -");
            }
            else {
                textPlatform.setText("");
            }

            if (itemViewType == TimelineItem.TYPE_STATION && !item.isVisible()) {
                convertView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
                convertView.setVisibility(View.GONE);
            }
            else {
                convertView.setVisibility(View.VISIBLE);
                convertView.setLayoutParams(new AbsListView.LayoutParams(-1,-2));
            }
        }
        else if (itemViewType == TimelineItem.TYPE_INSTRUCTION) {
            TextView textOperator = (TextView) convertView.findViewById(R.id.textOperator);
            textOperator.setText(item.getLeftOfLine());

            TextView textMain = (TextView) convertView.findViewById(R.id.textMain);
            textMain.setText(item.getText());

            TextView textExtra = (TextView) convertView.findViewById(R.id.textExtra);
            textExtra.setText(item.getExtraText());

            ImageView expandButton = (ImageView) convertView.findViewById(R.id.expandButton);
            if (expanded) {
                expandButton.setImageResource(R.drawable.unfold_less);
            }
            else {
                expandButton.setImageResource(R.drawable.unfold_more);
            }
        }
        else if (itemViewType == TimelineItem.TYPE_WALK) {
            TextView textMain = (TextView) convertView.findViewById(R.id.textMain);
            textMain.setText(item.getText());
        }

        if (itemViewType == TimelineItem.TYPE_INSTRUCTION) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setExpanded(!expanded);
                }
            });
        }
        else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing, hah!
                }
            });
        }

        return convertView;
    }
}
