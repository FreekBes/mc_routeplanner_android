package es.freekb.mc.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemPicker extends AppCompatActivity {
    private static final String tag_string_req = "autocomplete";

    private TextView searchInput;
    private ListView results;
    private TextView noResults;

    private String w = null;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;
    private boolean firstTextInputChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picker);

        searchInput = findViewById(R.id.searchInput);
        results = findViewById(R.id.results);
        noResults = findViewById(R.id.noResults);

        results.setVisibility(View.GONE);
        results.setEmptyView(noResults);

        Bundle extras = getIntent().getExtras();
        w = extras.getString("w", null);
        itemList = new ArrayList<Item>();
        itemAdapter = new ItemAdapter(ItemPicker.this, itemList);
        results.setAdapter(itemAdapter);

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                Item theChosenOne = itemAdapter.getItem(position);
                returnIntent.putExtra("item", theChosenOne.toJSONObject().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (firstTextInputChange) {
                    results.setVisibility(View.VISIBLE);
                }
                else {
                    cancelAutocompletions();
                }
                getAutocompletions(ItemPicker.this, s.toString());
            }
        });

        searchInput.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void cancelAutocompletions() {
        AppController.cancelPendingRequests("tag_string_req");
    }

    public void getAutocompletions(final Context ctx, String input) {
        if (!input.trim().equals("")) {
            String url = AppController.mainUrl + "api/autocomplete.php?i=" + input;
            if (w != null) {
                url += "&w=" + w;
            }

            StringRequest worldsGetter = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Item> items = new ArrayList<Item>();
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        if (responseJSON.getString("type").equals("success")) {
                            JSONArray data = responseJSON.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                items.add(new Item(data.getJSONObject(i)));
                            }
                            itemAdapter.updateItems(items);
                            Useful.setListViewHeightBasedOnChildren(results);
                        }
                        else {
                            itemAdapter.updateItems(items);
                        }
                    }
                    catch (JSONException e) {
                        itemAdapter.updateItems(items);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    itemAdapter.updateItems(new ArrayList<Item>());
                    error.printStackTrace();
                }
            }) {

            };

            AppController.addToRequestQueue(ctx, worldsGetter, tag_string_req);
        }
        else {
            itemAdapter.updateItems(new ArrayList<Item>());
        }
    }
}
