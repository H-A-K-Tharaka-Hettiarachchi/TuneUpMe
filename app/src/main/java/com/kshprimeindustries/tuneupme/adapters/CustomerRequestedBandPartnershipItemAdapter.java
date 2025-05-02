package com.kshprimeindustries.tuneupme.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerRequestedBandPartnershipItem;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CustomerRequestedBandPartnershipItemAdapter extends RecyclerView.Adapter<CustomerRequestedBandPartnershipItemAdapter.CustomerRequestedBandPartnershipItemViewHolder> {


    static class CustomerRequestedBandPartnershipItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRequestedBandPartnershipItemName;
        TextView textViewRequestedBandPartnershipItemType;
        TextView textViewRequestedBandPartnershipItemEmail;
        TextView textViewRequestedBandPartnershipItemMobile;
        TextView textViewRequestedBandPartnershipItemStatus;
        ImageView imageViewRequestedBandPartnershipItemDeleteButton;

        public CustomerRequestedBandPartnershipItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewRequestedBandPartnershipItemName = itemView.findViewById(R.id.textViewRequestedBandPartnershipItemName);
            this.textViewRequestedBandPartnershipItemType = itemView.findViewById(R.id.textViewRequestedBandPartnershipItemType);
            this.textViewRequestedBandPartnershipItemEmail = itemView.findViewById(R.id.textViewRequestedBandPartnershipItemEmail);
            this.textViewRequestedBandPartnershipItemMobile = itemView.findViewById(R.id.textViewRequestedBandPartnershipItemMobile);
            this.textViewRequestedBandPartnershipItemStatus = itemView.findViewById(R.id.textViewRequestedBandPartnershipItemStatus);
            this.imageViewRequestedBandPartnershipItemDeleteButton = itemView.findViewById(R.id.imageViewRequestedBandPartnershipItemDeleteButton);
        }
    }

    ArrayList<TuneUpMeCustomerRequestedBandPartnershipItem> tuneUpMeCustomerRequestedBandPartnershipItemArrayList;

    public CustomerRequestedBandPartnershipItemAdapter(ArrayList<TuneUpMeCustomerRequestedBandPartnershipItem> tuneUpMeCustomerRequestedBandPartnershipItemArrayList) {
        this.tuneUpMeCustomerRequestedBandPartnershipItemArrayList = tuneUpMeCustomerRequestedBandPartnershipItemArrayList;
    }

    @NonNull
    @Override
    public CustomerRequestedBandPartnershipItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.customer_requested_band_partnership_item, parent, false);

        return new CustomerRequestedBandPartnershipItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomerRequestedBandPartnershipItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textViewRequestedBandPartnershipItemName.setText(String.valueOf(tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getName()));
        holder.textViewRequestedBandPartnershipItemType.setText(String.valueOf(tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getBandType()));
        holder.textViewRequestedBandPartnershipItemEmail.setText(String.valueOf(tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getEmail()));
        holder.textViewRequestedBandPartnershipItemMobile.setText(String.valueOf(tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getMobile()));
        if (tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getBandReqStatus().equals("pending")) {
            holder.textViewRequestedBandPartnershipItemStatus.setText("Pending");
        } else if (tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getBandReqStatus().equals("rejected")) {
            holder.textViewRequestedBandPartnershipItemStatus.setText("Rejected");
        } else if (tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getBandReqStatus().equals("accepted")) {
            holder.textViewRequestedBandPartnershipItemStatus.setText("Accepted");
        }
        holder.imageViewRequestedBandPartnershipItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setPressed(true);
                v.invalidate();
                v.postDelayed(() -> v.setPressed(false), 200);

                v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100))
                        .start();

                String itemId = tuneUpMeCustomerRequestedBandPartnershipItemArrayList.get(position).getId();

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("band_request")
                        .document(itemId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                removeItem(position);
                                Toasty.success(v.getContext(), "Item Removed Success!", Toast.LENGTH_SHORT, true).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(v.getContext(), "Item Removed Failed!", Toast.LENGTH_SHORT, true).show();
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return tuneUpMeCustomerRequestedBandPartnershipItemArrayList.size();
    }

    public void removeItem(int position) {
        tuneUpMeCustomerRequestedBandPartnershipItemArrayList.remove(position);
        notifyItemRemoved(position);
    }

}
