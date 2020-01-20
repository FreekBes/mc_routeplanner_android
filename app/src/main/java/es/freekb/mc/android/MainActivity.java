package es.freekb.mc.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ITEM_PICKER_FROM = 1;
    private static final int ITEM_PICKER_TO = 2;

    private LinearLayout loadingLayout;
    private LinearLayout mainLayout;
    private TextView titleBar;
    private ListView fromInput;
    private TextView fromInputEmpty;
    private ListView toInput;
    private TextView toInputEmpty;
    private Button submitButton;

    private Handler handler;
    private Worlds worlds;
    private Planner planner;
    private Runnable fetchWorlds;

    private ItemAdapter fromItemAdapter;
    private ArrayList<Item> fromItemList;
    private ItemAdapter toItemAdapter;
    private ArrayList<Item> toItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingLayout = findViewById(R.id.loadingLayout);
        mainLayout = findViewById(R.id.mainLayout);
        titleBar = findViewById(R.id.titleBar);
        fromInput = findViewById(R.id.fromInput);
        fromInputEmpty = findViewById(R.id.fromInputEmpty);
        toInput = findViewById(R.id.toInput);
        toInputEmpty = findViewById(R.id.toInputEmpty);
        submitButton = findViewById(R.id.submitButton);

        fromItemList = new ArrayList<Item>();
        fromItemAdapter = new ItemAdapter(this, fromItemList);
        fromInput.setAdapter(fromItemAdapter);
        fromInput.setEmptyView(fromInputEmpty);
        fromInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startItemPicker(ITEM_PICKER_FROM);
            }
        });
        fromInputEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemPicker(ITEM_PICKER_FROM);
            }
        });

        toItemList = new ArrayList<Item>();
        toItemAdapter = new ItemAdapter(this, toItemList);
        toInput.setAdapter(toItemAdapter);
        toInput.setEmptyView(toInputEmpty);
        toInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startItemPicker(ITEM_PICKER_TO);
            }
        });
        toInputEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemPicker(ITEM_PICKER_TO);
            }
        });

        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plannerIntent = planner.getPlannerIntent(MainActivity.this);
                if (plannerIntent != null) {
                    startActivity(plannerIntent);
                }
            }
        });

        loadingLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        worlds = new Worlds(MainActivity.this);
        planner = new Planner(new Planner.PlannerListener() {
            @Override
            public void routeCalculated(Route route) {
                // wrong route calculated event! Use the listener in RouteActivity instead.
            }

            @Override
            public void routeCalculateError(String errorMessage) {
                // wrong event, see above
            }

            @Override
            public void readyStateChanged(boolean ready) {
                submitButton.setEnabled(ready);
            }
        });

        handler = new Handler();
        fetchWorlds = new Runnable() {
            public void run() {
                if (!worlds.worldsInitialized()) {
                    handler.postDelayed(fetchWorlds, 100);
                } else {
                    loadingLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    Worlds.World defaultWorld = worlds.getWorld(worlds.getWorldIDs().get(0));
                    planner.setWorld(defaultWorld.getId(), 0);
                    titleBar.setText(getString(R.string.app_name_with_world, defaultWorld.getDisplayName()));

                    titleBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(getString(R.string.main_world_selector_tooltip));
                            String[] worldNames = new String[worlds.getAmountOfWorlds()];
                            for (int i = 0; i < worlds.getAmountOfWorlds(); i++) {
                                worldNames[i] = worlds.getWorld(i).getName();
                            }
                            builder.setSingleChoiceItems(worldNames, planner.getCurrentWorldIndex(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Worlds.World selectedWorld = worlds.getWorld(which);
                                    planner.setWorld(selectedWorld.getId(), which);
                                    titleBar.setText(getString(R.string.app_name_with_world, selectedWorld.getDisplayName()));
                                    planner.setFrom(null);
                                    planner.setTo(null);
                                    updateFromAndToViews();
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do not change world, user clicked ok and is okay with the current world
                                }
                            });
                            builder.show();
                        }
                    });
                }
            }
        };
        fetchWorlds.run();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEM_PICKER_FROM) {
            if (resultCode == Activity.RESULT_OK && data.hasExtra("item")) {
                try {
                    planner.setFrom(new Item(new JSONObject(data.getStringExtra("item"))));
                    updateFromAndToViews();
                }
                catch (JSONException e) {
                    planner.setFrom(null);
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == ITEM_PICKER_TO) {
            if (resultCode == Activity.RESULT_OK && data.hasExtra("item")) {
                try {
                    planner.setTo(new Item(new JSONObject(data.getStringExtra("item"))));
                    updateFromAndToViews();
                }
                catch (JSONException e) {
                    planner.setTo(null);
                    e.printStackTrace();
                }
            }
        }
    }

    private void startItemPicker(int fromOrTo) {
        Intent i = new Intent(this, ItemPicker.class);
        i.putExtra("w", planner.getCurrentWorldId());
        startActivityForResult(i, fromOrTo);
    }

    private void updateFromAndToViews() {
        fromItemList.clear();
        if (!planner.fromEmpty()) {
            fromItemList.add(planner.getFrom());
        }
        fromItemAdapter.notifyDataSetChanged();

        toItemList.clear();
        if (!planner.toEmpty()) {
            toItemList.add(planner.getTo());
        }
        toItemAdapter.notifyDataSetChanged();
    }
}
