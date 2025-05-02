package com.kshprimeindustries.tuneupme.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCurrentBandScheduleItem;

import java.util.ArrayList;

public class CurrentBandScheduleItemAdapter extends RecyclerView.Adapter<CurrentBandScheduleItemAdapter.CurrentBandScheduleItemViewHolder> {


    static class CurrentBandScheduleItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCurrentBandScheduleBandName;
        TextView textViewCurrentBandScheduleStartDateTime;
        TextView textViewCurrentBandScheduleEndDateTime;
        TextView textViewCurrentBandScheduleBookingConfirmed;

        public CurrentBandScheduleItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewCurrentBandScheduleBandName = itemView.findViewById(R.id.textViewCurrentBandScheduleBandName);
            this.textViewCurrentBandScheduleStartDateTime = itemView.findViewById(R.id.textViewCurrentBandScheduleStartDateTime);
            this.textViewCurrentBandScheduleEndDateTime = itemView.findViewById(R.id.textViewCurrentBandScheduleEndDateTime);
            this.textViewCurrentBandScheduleBookingConfirmed = itemView.findViewById(R.id.textViewCurrentBandScheduleBookingConfirmed);
        }
    }

    ArrayList<TuneUpMeCurrentBandScheduleItem> currentBandScheduleItemViewHolderArrayList;

    public CurrentBandScheduleItemAdapter(ArrayList<TuneUpMeCurrentBandScheduleItem> currentBandScheduleItemViewHolderArrayList) {
        this.currentBandScheduleItemViewHolderArrayList = currentBandScheduleItemViewHolderArrayList;
    }

    @NonNull
    @Override
    public CurrentBandScheduleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View scheduleItem = layoutInflater.inflate(R.layout.tune_up_me_band_current_band_schedule_item, parent, false);
        return new CurrentBandScheduleItemViewHolder(scheduleItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CurrentBandScheduleItemViewHolder holder, int position) {
        TuneUpMeCurrentBandScheduleItem tuneUpMeCurrentBandScheduleItem = currentBandScheduleItemViewHolderArrayList.get(position);
        holder.textViewCurrentBandScheduleBandName.setText(String.valueOf(tuneUpMeCurrentBandScheduleItem.getCurrentBandScheduleBandName()));
        holder.textViewCurrentBandScheduleStartDateTime.setText(String.valueOf(tuneUpMeCurrentBandScheduleItem.getCurrentBandScheduleStartDateTime()));
        holder.textViewCurrentBandScheduleEndDateTime.setText(String.valueOf(tuneUpMeCurrentBandScheduleItem.getCurrentBandScheduleEndDateTime()));
        if (tuneUpMeCurrentBandScheduleItem.isCurrentBandScheduleBookingConfirmed()) {
            holder.textViewCurrentBandScheduleBookingConfirmed.setText("Booked");
        } else {
            holder.textViewCurrentBandScheduleBookingConfirmed.setText("Booking Not Confirmed");
        }
    }

    @Override
    public int getItemCount() {
        return currentBandScheduleItemViewHolderArrayList.size();
    }


}
