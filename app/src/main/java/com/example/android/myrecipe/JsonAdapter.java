package com.example.android.myrecipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<RecipeItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public JsonAdapter(Context context, ArrayList<RecipeItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        RecipeItem currentItem = mExampleList.get(position);


        String imageUrl = currentItem.getmImageUrl();
        String recipeName = currentItem.getmTitle();
        double socialRank = currentItem.getmRank();

        holder.mTextViewTitle.setText(recipeName);
        holder.mTextViewRank.setText(String.format("%.2f", socialRank));
        Picasso.get().load(imageUrl).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    // responsible for displaying in MainActivity
    // converts texts -> textView
    // converts image string -> imageView
    // can reference to MainActivity
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewRank;

        // we use itemView to get references on the example_item.xml
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewRank = itemView.findViewById(R.id.text_view_rank);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                            Log.d("JsonAdapter", "onClick: ");
                        }
                    }
                }
            });
        }


    }
}
