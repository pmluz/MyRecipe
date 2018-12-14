package com.example.android.myrecipe;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IngredientsUtils {
    private static List<String> mIngredientsList;
    private JsonAdapter mJsonAdapter;
    private RecyclerView mRecyclerView;
    private static RequestQueue mRequestQueue;
    private static String newString;

    /* static class members: parsing the url */
    // https://www.food2fork.com/api/get?key=07431e3407ae31101f1e5b7ecf495c65&rId=35382
    final static String BASE_URL = "https://www.food2fork.com/api/get";
    final static String QUERY_KEY = "key";
    final static String API_KEY = "6dc7e7cde79dadadc2a7a3173ab6f947";
    final static String RECIPE_ID = "rId";

    public IngredientsUtils(String nString) {
        newString = nString;
    }

    public static String getNewString() {
        return newString;
    }

    /* Method that uses Uri.Builder to build appropriate url,
     * same as the one from above
     * Returns a Java URL object
     */
    public static URL buildURL(String rId) {
        Uri buildURI = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_KEY, API_KEY)
                .appendQueryParameter(RECIPE_ID, rId)
                .build();
        URL url = null;
        try {
            url = new URL(buildURI.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("buildURL", "buildURI: " + url);
        return url;
    }

    // api call
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return  scanner.next();
            }
            else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

    //    // parse the ingredients list
    public static List<String> parseIngredientsList(String JSONString) {
        List<String> ingredientsList = new ArrayList<>();
        newString = "";

        try {
            JSONObject main = new JSONObject(JSONString);
            JSONObject recipe = main.getJSONObject("recipe");
            JSONArray ingredient = recipe.getJSONArray("ingredients");

            for(int i = 0; i < ingredient.length(); i++) {
                ingredientsList.add(ingredient.getString(i));
            }

            Log.d("parseJSON", "parseIngredientsList: " + ingredientsList.toString());
            mIngredientsList = ingredientsList;
            Log.d("mIngredientsList", "parseIngredientsList: " + mIngredientsList.toString());
            Log.d("notEmpty", "buildList: " + mIngredientsList);

            String separator = "\n";
            StringBuilder stringBuilder = new StringBuilder();

            for(String i: mIngredientsList) {
                stringBuilder.append(i);
                stringBuilder.append(separator);
            }
            newString = stringBuilder.toString();
            Log.d("stringBuilder", "newString: " + newString);


        } catch (JSONException e) {
            e.printStackTrace();
        }
//        return Collections.singletonList(newString);
        return ingredientsList;
//        return Collections.singletonList(ingredientsList.toString());
    }


}