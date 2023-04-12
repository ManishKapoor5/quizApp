package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    Context context;
    List<Item> postList;

    public PostAdapter(Context context, List<Item> postList){
        this.context = context;
        this.postList = postList;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.eachpost , parent , false)    ;
        return new PostHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        Item item = postList.get(position);
        holder.setImageView(item.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        View view;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setImageView(String url){
            imageView = view.findViewById(R.id.imageview);
            Glide.with(context).load(url).into(imageView);
        }


    }
}
