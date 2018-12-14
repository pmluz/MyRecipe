package com.example.android.myrecipe;

public class RecipeItem {
    private String mImageUrl;
    private String mPublisher;
    private double mRank;
    private String mTitle;
    private String mSourceUrl;
    private String mRecipeId;


    public RecipeItem(String imageUrl, String publisher, double rank , String title, String sourceUrl, String recipeId) {
        mImageUrl = imageUrl;
        mPublisher = publisher;
        mRank = rank;
        mTitle = title;
        mSourceUrl = sourceUrl;
        mRecipeId = recipeId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public double getmRank() {
        return mRank;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSourceUrl() {
        return mSourceUrl;
    }

    public String getmRecipeId() {
        return mRecipeId;
    }
}
