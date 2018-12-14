package com.example.android.myrecipe;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.android.myrecipe.MainActivity.EXTRA_IMAGE;
import static com.example.android.myrecipe.MainActivity.EXTRA_PUBLISHER;
import static com.example.android.myrecipe.MainActivity.EXTRA_RANK;
import static com.example.android.myrecipe.MainActivity.EXTRA_RECIPE_ID;
import static com.example.android.myrecipe.MainActivity.EXTRA_SRC_URL;
import static com.example.android.myrecipe.MainActivity.EXTRA_TITLE;
import static com.example.android.myrecipe.MainActivity.EXTRA_IMAGE;

/* displays the specific recipe in the activity_detail.xml */
public class DetailActivity extends AppCompatActivity {
    double socialRank;
    List<String> recipeItemList;
    String recipes = IngredientsUtils.getNewString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // catches the intent from the mainActivity onClickItem()
        // displays all the info in the activity_detail.xml
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_IMAGE);
        String creatorName = intent.getStringExtra(EXTRA_PUBLISHER);
        double test = intent.getDoubleExtra(EXTRA_RANK, socialRank);
        String recipeName = intent.getStringExtra(EXTRA_TITLE);
        String sourceUrl = intent.getStringExtra(EXTRA_SRC_URL);
        String recipeId = intent.getStringExtra(EXTRA_RECIPE_ID);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewRank = findViewById(R.id.text_view_rank_detail);
        TextView textViewTitle = findViewById(R.id.text_view_title_detail);
        TextView textViewSourceUrl = findViewById(R.id.text_view_source_url_detail);
        TextView textViewIngredients = findViewById(R.id.text_view_ingredients_detail);

        newsSearchQuery(recipeId);

        Log.d("showIngredients", "onCreate: " + recipes);
        Picasso.get().load(imageUrl).into(imageView);
        textViewTitle.setText(recipeName);
        textViewCreator.setText(creatorName);
        textViewRank.setText(String.format("%.2f", test));
        textViewSourceUrl.setText(sourceUrl);
        textViewIngredients.setText(recipes);

    }

    // trying to see if when item is clicked it will open to the webpage
    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, webpage);

        if(webPageIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webPageIntent);
        }
    }

    public void newsSearchQuery(String recipeId) {
        URL url = IngredientsUtils.buildURL(recipeId);
        Log.d("recipeItemList", "value: " + recipeItemList);
        new NewsQueryTask().execute(url);
    }

    class NewsQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;
            try {
                results = IngredientsUtils.getResponseFromHttpUrl(url);
                Log.d("ASYNC", "doInBackground: " + results);
            } catch(IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            delete(); // clears all current entries in the database
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            recipeItemList = IngredientsUtils.parseIngredientsList(s);
//            insert(articles);
            Log.d("onPostExecute", "onPostExecute: " + recipeItemList);
        }
    }
}