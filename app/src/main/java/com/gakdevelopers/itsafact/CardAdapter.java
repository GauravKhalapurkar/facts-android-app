package com.gakdevelopers.itsafact;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    CardData[] cardData;
    Context context;

    public CardAdapter(CardData[] cardData, MainActivity activity) {
        this.cardData = cardData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CardData cardDataList = cardData[position];
        holder.txtCategoryName.setText(cardDataList.getCategoryName());
        holder.imgCategoryImg.setImageResource(cardDataList.getCategoryImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FactsActivity.class);
                intent.putExtra("categoryName", cardDataList.getCategoryName());
                intent.putExtra("categoryImg", cardDataList.getCategoryImg());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategoryName;
        ImageView imgCategoryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            imgCategoryImg = itemView.findViewById(R.id.imgCategoryImg);
        }
    }
}
