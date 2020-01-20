package es.freekb.mc.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView titleText;
    private ListView timeline;
    private LinearLayout loadingTimeline;

    private Planner planner;
    private Route route;
    private TimelineAdapter timelineAdapter;
    private ArrayList<TimelineItem> timelineItems;
    private boolean routeReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        backButton = findViewById(R.id.backButton);
        titleText = findViewById(R.id.titleText);
        timeline = findViewById(R.id.timeline);
        loadingTimeline = findViewById(R.id.loadingTimeline);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timelineItems = new ArrayList<TimelineItem>();
        timelineAdapter = new TimelineAdapter(RouteActivity.this, timelineItems);
        timeline.setEmptyView(loadingTimeline);
        timeline.setAdapter(timelineAdapter);

        Bundle extras = getIntent().getExtras();
        planner = new Planner(new Planner.PlannerListener() {
            @Override
            public void routeCalculated(Route calculatedRoute) {
                routeReady = true;
                route = calculatedRoute;
                boolean toDoStartItem = true;
                boolean toDoEndItem = true;
                Item walkStartItem = null;
                Item walkEndItem = null;
                RouteData routeData = route.getRouteData();

                try {
                    if (route.getWalking().getJSONObject("from").getBoolean("required")) {
                        walkStartItem = (Item) route.getItems().get(route.getWalking().getJSONObject("from").getString("start"));
                        walkEndItem = (Item) route.getItems().get(route.getWalking().getJSONObject("from").getString("end"));

                        TimelineItem walkingStartItem = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                        walkingStartItem.setConnection(TimelineItem.CONNECTION_TOP);
                        walkingStartItem.setLeftOfLine("");
                        walkingStartItem.setText(walkStartItem.getName());
                        walkingStartItem.setPlatformNumber(-1);
                        timelineItems.add(walkingStartItem);
                        toDoStartItem = false;

                        TimelineItem walkInstructions = new TimelineItem(TimelineItem.TYPE_WALK);
                        walkInstructions.setText(getString(R.string.timeline_walk_instructions, Useful.getDistance(walkStartItem.getCoords(), walkEndItem.getCoords()), walkEndItem.getTypeIconAndName()[1].toLowerCase(), walkEndItem.getName(), walkEndItem.getCoords()[0], walkEndItem.getCoords()[1], walkEndItem.getCoords()[2]));
                        timelineItems.add(walkInstructions);
                    }

                    if (route.getWalking().getJSONObject("to").getBoolean("required")) {
                        toDoEndItem = false;
                    }

                    if (routeData != null) {
                        String lastLine = null;
                        int totalDuration = 0;
                        boolean lineGroupFirstStationAdded = false;
                        ArrayList<Warning> lineWarningsCombined = new ArrayList<Warning>();
                        int haltAmount = routeData.getHalts().size();
                        for (int i = 0; i < haltAmount; i++) {
                            if (i != haltAmount - 1 && !routeData.getLines().get(i).equals(lastLine)) {
                                lineGroupFirstStationAdded = false;
                                lineWarningsCombined.clear();
                            }

                            if (i == 0) {
                                TimelineItem firstHalt = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                                if (toDoStartItem) {
                                    firstHalt.setConnection(TimelineItem.CONNECTION_TOP);
                                }
                                firstHalt.setLeftOfLine(Useful.formatTime(RouteActivity.this, Useful.getTimeOfArrival(0)));
                                firstHalt.setText(((Item) route.getItems().get(routeData.getHalts().get(i))).getName());
                                firstHalt.setPlatformNumber(routeData.getPlatforms().get(i));
                                timelineItems.add(firstHalt);
                                lineGroupFirstStationAdded = true;
                                if (routeData.getWarningsForRoutePart(i).size() > 0) {
                                    lineWarningsCombined.addAll(routeData.getWarningsForRoutePart(i));
                                }
                            }
                            else if (i == haltAmount - 1) {
                                totalDuration += routeData.getDurations().get(i - 1);
                                TimelineItem endHalt = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                                if (toDoEndItem) {
                                    endHalt.setConnection(TimelineItem.CONNECTION_BOTTOM);
                                }
                                endHalt.setLeftOfLine(Useful.formatTime(RouteActivity.this, Useful.getTimeOfArrival(totalDuration)));
                                endHalt.setText(((Item) route.getItems().get(routeData.getHalts().get(i))).getName());
                                endHalt.setPlatformNumber(routeData.getPlatforms().get(i*2-1));
                                timelineItems.add(endHalt);
                            }
                            else {
                                totalDuration += routeData.getDurations().get(i - 1);
                                TimelineItem halt;
                                if (!routeData.getLines().get(i).equals(lastLine)) {
                                    lineGroupFirstStationAdded = true;
                                    halt = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                                }
                                else {
                                    halt = new TimelineItem(TimelineItem.TYPE_STATION);
                                }
                                halt.setLeftOfLine(Useful.formatTime(RouteActivity.this, Useful.getTimeOfArrival(totalDuration)));
                                halt.setText(((Item) route.getItems().get(routeData.getHalts().get(i))).getName());
                                halt.setPlatformNumber(routeData.getPlatforms().get(i*2));
                                timelineItems.add(halt);
                            }

                            if (i < haltAmount - 1) {
                                if (!routeData.getLines().get(i).equals(lastLine)) {
                                    TimelineItem newLineInstructions = new TimelineItem(TimelineItem.TYPE_INSTRUCTION);
                                    Line lineToTake = (Line) route.getLines().get(routeData.getLines().get(i));
                                    newLineInstructions.setLeftOfLine(lineToTake.getOperator());
                                    newLineInstructions.setText(lineToTake.getOperator() + " " + lineToTake.getTypeReadable(RouteActivity.this).toLowerCase() + " " + lineToTake.getName());
                                    newLineInstructions.setExtraText(getString(R.string.internal_direction, ((Item) route.getItems().get(routeData.getHalts().get(i+1))).getName()));
                                    timelineItems.add(newLineInstructions);
                                }
                                lastLine = routeData.getLines().get(i);
                            }
                        }
                    }
                    else {
                        // no public transport needed for this route
                        // just walk
                        TimelineItem walkingEndItem = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                        walkingEndItem.setConnection(TimelineItem.CONNECTION_BOTTOM);
                        walkingEndItem.setLeftOfLine("");
                        walkingEndItem.setText(walkEndItem.getName());
                        walkingEndItem.setPlatformNumber(-1);
                        timelineItems.add(walkingEndItem);
                    }

                    if (route.getWalking().getJSONObject("to").getBoolean("required")) {
                        walkStartItem = (Item) route.getItems().get(route.getWalking().getJSONObject("to").getString("start"));
                        walkEndItem = (Item) route.getItems().get(route.getWalking().getJSONObject("to").getString("end"));

                        TimelineItem walkInstructions = new TimelineItem(TimelineItem.TYPE_WALK);
                        walkInstructions.setText(getString(R.string.timeline_walk_instructions, Useful.getDistance(walkStartItem.getCoords(), walkEndItem.getCoords()), walkEndItem.getTypeIconAndName()[1].toLowerCase(), walkEndItem.getName(), walkEndItem.getCoords()[0], walkEndItem.getCoords()[1], walkEndItem.getCoords()[2]));
                        timelineItems.add(walkInstructions);

                        TimelineItem walkingEndItem = new TimelineItem(TimelineItem.TYPE_STATION_TRANSFER);
                        walkingEndItem.setConnection(TimelineItem.CONNECTION_BOTTOM);
                        walkingEndItem.setLeftOfLine("");
                        walkingEndItem.setText(walkEndItem.getName());
                        walkingEndItem.setPlatformNumber(-1);
                        timelineItems.add(walkingEndItem);
                    }

                    timelineAdapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    couldNotDisplayRoute();
                }
            }

            @Override
            public void routeCalculateError(String errorMessage) {
                new AlertDialog.Builder(RouteActivity.this)
                        .setMessage(errorMessage)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        })
                        .show();
            }

            @Override
            public void readyStateChanged(boolean ready) {
                if (ready) {
                    planner.plan(RouteActivity.this);
                }
            }
        });
        if (extras.containsKey("world") && extras.containsKey("worldIndex") && extras.containsKey("from") && extras.containsKey("to")) {
            try {
                planner.setWorld(extras.getString("world", null), extras.getInt("worldIndex", 0));
                planner.setFrom(new Item(new JSONObject(extras.getString("from"))));
                planner.setTo(new Item(new JSONObject(extras.getString("to"))));
                titleText.setText(getString(R.string.internal_route_from_to, planner.getFrom().getName(), planner.getTo().getName()));
                // actual planning starts in PlannerListener readyStateChanged
            }
            catch (JSONException e) {
                AppController.unknownError(RouteActivity.this);
            }
        }
        else {
            AppController.unknownError(RouteActivity.this);
        }
    }

    @Override
    public void onDestroy() {
        planner.cancelPlanning();
        if (routeReady) {
            setResult(Activity.RESULT_OK);
        }
        else {
            setResult(Activity.RESULT_CANCELED);
        }
        super.onDestroy();
    }

    private void couldNotDisplayRoute() {
        new AlertDialog.Builder(RouteActivity.this)
                .setMessage(getString(R.string.alert_could_not_display_route))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }
}
