package com.kshprimeindustries.tuneupme.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityChat;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityLocationViewer;
import com.kshprimeindustries.tuneupme.model.TuneUpMeBandMyScheduleItem;

import java.util.ArrayList;

public class TuneUpMeBandMyScheduleItemAdapter extends RecyclerView.Adapter<TuneUpMeBandMyScheduleItemAdapter.TuneUpMeBandMyScheduleItemViewHolder> {

    static class TuneUpMeBandMyScheduleItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBandBookedScheduleItemCustomerName;
        TextView textViewBandBookedScheduleItemCustomerEmail;
        TextView textViewBandBookedScheduleItemCustomerMobile;
        TextView textViewBandBookedScheduleItemEventStartDateTime;
        TextView textViewBandBookedScheduleItemEventEndDateTime;
        TextView textViewBandBookedScheduleItemPaymentStatus;
        ImageView imageViewBandBookedScheduleItemViewLocationButton;
        TextView textViewBandBookedScheduleItemMessageNowButton;
        TextView textViewBandBookedScheduleItemCallNowButton;

        public TuneUpMeBandMyScheduleItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewBandBookedScheduleItemCustomerName = itemView.findViewById(R.id.textViewBandBookedScheduleItemCustomerName);
            this.textViewBandBookedScheduleItemCustomerEmail = itemView.findViewById(R.id.textViewBandBookedScheduleItemCustomerEmail);
            this.textViewBandBookedScheduleItemCustomerMobile = itemView.findViewById(R.id.textViewBandBookedScheduleItemCustomerMobile);
            this.textViewBandBookedScheduleItemEventStartDateTime = itemView.findViewById(R.id.textViewBandBookedScheduleItemEventStartDateTime);
            this.textViewBandBookedScheduleItemEventEndDateTime = itemView.findViewById(R.id.textViewBandBookedScheduleItemEventEndDateTime);
            this.textViewBandBookedScheduleItemPaymentStatus = itemView.findViewById(R.id.textViewBandBookedScheduleItemPaymentStatus);
            this.imageViewBandBookedScheduleItemViewLocationButton = itemView.findViewById(R.id.imageViewBandBookedScheduleItemViewLocationButton);
            this.textViewBandBookedScheduleItemMessageNowButton = itemView.findViewById(R.id.textViewBandBookedScheduleItemMessageNowButton);
            this.textViewBandBookedScheduleItemCallNowButton = itemView.findViewById(R.id.textViewBandBookedScheduleItemCallNowButton);
        }
    }

    ArrayList<TuneUpMeBandMyScheduleItem> tuneUpMeBandMyScheduleItemArrayList;

    public TuneUpMeBandMyScheduleItemAdapter(ArrayList<TuneUpMeBandMyScheduleItem> tuneUpMeBandMyScheduleItemArrayList) {
        this.tuneUpMeBandMyScheduleItemArrayList = tuneUpMeBandMyScheduleItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeBandMyScheduleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.band_booked_schedule_item, parent, false);
        return new TuneUpMeBandMyScheduleItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TuneUpMeBandMyScheduleItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textViewBandBookedScheduleItemCustomerName.setText(tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerName());
        holder.textViewBandBookedScheduleItemCustomerEmail.setText(tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerEmail());
        holder.textViewBandBookedScheduleItemCustomerMobile.setText(tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerMobile());
        holder.textViewBandBookedScheduleItemEventStartDateTime.setText(tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerEventStartDateTime());
        holder.textViewBandBookedScheduleItemEventEndDateTime.setText(tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerEventEndDateTime());

        if (tuneUpMeBandMyScheduleItemArrayList.get(position).isPaymentStatus()) {
            holder.textViewBandBookedScheduleItemPaymentStatus.setText("Payed");
        } else {
            holder.textViewBandBookedScheduleItemPaymentStatus.setText("Not Payed");
        }

        holder.imageViewBandBookedScheduleItemViewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityLocationViewer.class);
                intent.putExtra("latitude", tuneUpMeBandMyScheduleItemArrayList.get(position).getLatitude());
                intent.putExtra("longitude", tuneUpMeBandMyScheduleItemArrayList.get(position).getLongitude());
                v.getContext().startActivity(intent);
            }
        });
        holder.textViewBandBookedScheduleItemMessageNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityChat.class);
                intent.putExtra("user_id", tuneUpMeBandMyScheduleItemArrayList.get(position).getBandId());
                intent.putExtra("guest_id", tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerId());
                intent.putExtra("guest_type", "band");
                v.getContext().startActivity(intent);
            }
        });
        holder.textViewBandBookedScheduleItemCallNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = "tel:" + tuneUpMeBandMyScheduleItemArrayList.get(position).getCustomerMobile();
                Uri uri = Uri.parse(mobile);
                intent.setData(uri);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tuneUpMeBandMyScheduleItemArrayList.size();
    }

    private void animateCardClick(View view) {
        view.setPressed(true);
        view.invalidate();
        view.postDelayed(() -> view.setPressed(false), 200);

        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100))
                .start();
    }

}
