package com.example.android.myrecipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JsonAdapter.OnItemClickListener {
    // ff will be used for OnItemClick()
    public static final String EXTRA_IMAGE = "imageUrl";
    public static final String EXTRA_PUBLISHER = "publisherName";
    public static final String EXTRA_RANK = "socialRank";
    public static final String EXTRA_TITLE = "recipeName";
    public static final String EXTRA_SRC_URL = "sourceUrl";
    public static final String EXTRA_RECIPE_ID = "recipeId";


    private EditText mSearchBoxEditText;

    private RecyclerView mRecyclerView;
    private JsonAdapter mJsonAdapter;
    private ArrayList<RecipeItem> mExampleList;
    private RequestQueue mRequestQueue;
    private RecipeItem recipeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
     //   parseJSON();


    }

    // responsible for retrieving specific data from the json file
    private void parseJSON() {
       // String url = "https://www.food2fork.com/api/search?key=4a8ff1b5e856502c2b3f32bc39c8ef6f&q=chicken%20breast&page=2";

        //BUILDING URL FROM NOTHING
        String inputString =  mSearchBoxEditText.getText().toString();


        String baseURL = "https://www.food2fork.com/api/search?key=";
        String key = "6dc7e7cde79dadadc2a7a3173ab6f947";
        String qBase = "&q=";
        String[] splitInput = inputString.split(" ");
        StringBuilder sBuildInput = new StringBuilder();
        for(String i: splitInput)
        {
            sBuildInput.append(i);
            sBuildInput.append("%20");
        }
        String foodItem = sBuildInput.toString();
        String page = "&page=1";

        String url = baseURL+key+qBase+foodItem+page;



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("recipes");

                    // passing the JSON data to the ExampleList
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject hit = jsonArray.getJSONObject(i);

                        String recipeName = hit.getString("title");
                        String publisherName = hit.getString("publisher");
                        String imageUrl = hit.getString("image_url");
                        double socialRank = hit.getDouble("social_rank");
                        String sourceUrl = hit.getString("source_url");
                        String recipeId = hit.getString("recipe_id");


                        mExampleList.add(new RecipeItem(imageUrl, publisherName, socialRank, recipeName, sourceUrl, recipeId));
                    }

                    // passing the data to the adapter
                    mJsonAdapter = new JsonAdapter(MainActivity.this, mExampleList);
                    // passing the data to the recyclerView
                    mRecyclerView.setAdapter(mJsonAdapter);

                    // implementing the interface from the JsonAdapter
                    mJsonAdapter.setOnItemClickListener(MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // add request to the mRequestQueue
        mRequestQueue.add(request);
    }

    // getting called at the DetailActivity class
    // responsible for displaying all data in the activity_detail.xml
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        RecipeItem clickedItem = mExampleList.get(position);
        Log.d("RANK", "onItemClick: " + clickedItem.getmRank());

        detailIntent.putExtra(EXTRA_IMAGE, clickedItem.getmImageUrl());
        detailIntent.putExtra(EXTRA_TITLE, clickedItem.getmTitle());
        detailIntent.putExtra(EXTRA_PUBLISHER, clickedItem.getmPublisher());
        detailIntent.putExtra(EXTRA_RANK, clickedItem.getmRank());
        detailIntent.putExtra(EXTRA_RECIPE_ID, clickedItem.getmRecipeId());
        detailIntent.putExtra(EXTRA_SRC_URL, clickedItem.getmSourceUrl());
        startActivity(detailIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        String searchQuery = mSearchBoxEditText.getText().toString();
        Log.d("QUERY", "onOptionsItemSelected: " + mSearchBoxEditText.getText().toString());
        if (itemThatWasClickedId == R.id.action_search) {
            parseJSON();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
