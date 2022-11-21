package com.example.numad22fa_team49_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.R;

public class GeneralProductHomeAdapter extends RecyclerView.Adapter<GeneralProductHomeAdapter.GeneralProductHomeViewHolder> {

    Context context;

    public GeneralProductHomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GeneralProductHomeAdapter.GeneralProductHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralProductHomeAdapter.GeneralProductHomeViewHolder(LayoutInflater.from(context).inflate(R.layout.general_product_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralProductHomeAdapter.GeneralProductHomeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class GeneralProductHomeViewHolder extends RecyclerView.ViewHolder {
        public GeneralProductHomeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
