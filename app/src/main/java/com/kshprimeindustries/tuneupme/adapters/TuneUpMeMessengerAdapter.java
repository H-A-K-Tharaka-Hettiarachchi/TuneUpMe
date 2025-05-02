package com.kshprimeindustries.tuneupme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.model.TuneUpMeMessengerItem;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class TuneUpMeMessengerAdapter extends RecyclerView.Adapter<TuneUpMeMessengerAdapter.TuneUpMeMessengerViewHolder> {


    static class TuneUpMeMessengerViewHolder extends RecyclerView.ViewHolder {

        public CardView cardViewTuneUpMeMessengerItemMain;
        public ImageView imageViewTuneUpMeMessengerItemProfilePicture;
        public TextView textViewTuneUpMeMessengerItemProfileName;

        public TextView textViewTuneUpMeMessengerItemMessageTime;

        public ImageView imageViewTuneUpMeMessengerItemMessageStatus;

        public TextView textViewTuneUpMeMessengerItemMessage;

        public TextView textViewTuneUpMeMessengerItemMessageCount;


        public TuneUpMeMessengerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewTuneUpMeMessengerItemMain = itemView.findViewById(R.id.cardViewTuneUpMeMessengerItemMain);
            imageViewTuneUpMeMessengerItemProfilePicture = itemView.findViewById(R.id.imageViewTuneUpMeMessengerItemProfilePicture);
            textViewTuneUpMeMessengerItemProfileName = itemView.findViewById(R.id.textViewTuneUpMeMessengerItemProfileName);
            textViewTuneUpMeMessengerItemMessageTime = itemView.findViewById(R.id.textViewTuneUpMeMessengerItemMessageTime);
            imageViewTuneUpMeMessengerItemMessageStatus = itemView.findViewById(R.id.imageViewTuneUpMeMessengerItemMessageStatus);
            textViewTuneUpMeMessengerItemMessage = itemView.findViewById(R.id.textViewTuneUpMeMessengerItemMessage);
            textViewTuneUpMeMessengerItemMessageCount = itemView.findViewById(R.id.textViewTuneUpMeMessengerItemMessageCount);
        }

    }


    public ArrayList<TuneUpMeMessengerItem> tuneUpMeMessengerItemArrayList;

    public TuneUpMeMessengerAdapter(ArrayList<TuneUpMeMessengerItem> tuneUpMeMessengerItemArrayList) {
        this.tuneUpMeMessengerItemArrayList = tuneUpMeMessengerItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeMessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tune_up_me_messenger_item, parent, false);

        TuneUpMeMessengerViewHolder tuneUpMeMessengerViewHolder = new TuneUpMeMessengerViewHolder(view);

        return tuneUpMeMessengerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TuneUpMeMessengerViewHolder holder, int position) {
        holder.textViewTuneUpMeMessengerItemProfileName.setText(tuneUpMeMessengerItemArrayList.get(position).getTuneUpMeMessengerItemProfileName());
        holder.textViewTuneUpMeMessengerItemMessage.setText(tuneUpMeMessengerItemArrayList.get(position).getTuneUpMeMessengerItemMessage());
        holder.textViewTuneUpMeMessengerItemMessageTime.setText(tuneUpMeMessengerItemArrayList.get(position).getTuneUpMeMessengerItemMessageTime());
        holder.textViewTuneUpMeMessengerItemMessageCount.setText(tuneUpMeMessengerItemArrayList.get(position).getTuneUpMeMessengerItemMessageCount());
        holder.imageViewTuneUpMeMessengerItemProfilePicture.setImageResource(Integer.parseInt(tuneUpMeMessengerItemArrayList.get(position).getTuneUpMeMessengerItemProfilePicture()));

        if (tuneUpMeMessengerItemArrayList.get(position).isTuneUpMeMessengerItemMessageStatus()) {
            holder.imageViewTuneUpMeMessengerItemMessageStatus.setImageResource(R.drawable.ic_message_seen);
        } else {
            holder.imageViewTuneUpMeMessengerItemMessageStatus.setImageResource(R.drawable.ic_message_delivered);
        }

        holder.cardViewTuneUpMeMessengerItemMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(v.getContext(), "Item Clicked!", Toast.LENGTH_SHORT, true).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return tuneUpMeMessengerItemArrayList.size();
    }


}
