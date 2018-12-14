package com.example.android.myrecipe;

import java.util.List;

public class IngredientsItem {
    List<String> mIngredients;
    public IngredientsItem(List<String> ingredients) {
        mIngredients = ingredients;
    }

    public IngredientsItem(String ingredients) {
    }

    public List<String> getmIngredients() {
        return mIngredients;
    }
}
