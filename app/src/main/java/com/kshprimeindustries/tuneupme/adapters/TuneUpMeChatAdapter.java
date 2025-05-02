package com.kshprimeindustries.tuneupme.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TuneUpMeChatAdapter   extends RecyclerView.Adapter<TuneUpMeChatAdapter.TuneUpMeChatViewHolder> {


    static  class  TuneUpMeChatViewHolder extends RecyclerView.ViewHolder{

        public TuneUpMeChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    @NonNull
    @Override
    public TuneUpMeChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TuneUpMeChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
