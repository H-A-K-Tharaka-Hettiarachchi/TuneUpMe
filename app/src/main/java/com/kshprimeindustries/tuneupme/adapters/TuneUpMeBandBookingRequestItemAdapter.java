package com.kshprimeindustries.tuneupme.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityChat;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityLocationViewer;
import com.kshprimeindustries.tuneupme.model.TuneUpMeBandBookingRequestItem;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class TuneUpMeBandBookingRequestItemAdapter extends RecyclerView.Adapter<TuneUpMeBandBookingRequestItemAdapter.TuneUpMeBandBookingRequestItemViewHolder> {

    static class TuneUpMeBandBookingRequestItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBandBookingRequestItemCustomerName;
        TextView textViewBandBookingRequestItemCustomerEmail;
        TextView textViewBandBookingRequestItemCustomerMobile;
        TextView textViewBandBookingRequestItemEventStartDateTime;
        TextView textViewBandBookingRequestItemEventEndDateTime;
        ImageView textViewBandBookingRequestItemViewLocationButton;
        TextView textViewBandBookingRequestItemMessageNowButton;
        TextView textViewBandBookingRequestItemCallNowButton;
        TextView textViewBandBookingRequestAcceptButton;
        TextView textViewBandBookingRequestDeclineButton;

        public TuneUpMeBandBookingRequestItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewBandBookingRequestItemCustomerName = itemView.findViewById(R.id.textViewBandBookingRequestItemCustomerName);
            this.textViewBandBookingRequestItemCustomerEmail = itemView.findViewById(R.id.textViewBandBookingRequestItemCustomerEmail);
            this.textViewBandBookingRequestItemCustomerMobile = itemView.findViewById(R.id.textViewBandBookingRequestItemCustomerMobile);
            this.textViewBandBookingRequestItemEventStartDateTime = itemView.findViewById(R.id.textViewBandBookingRequestItemEventStartDateTime);
            this.textViewBandBookingRequestItemEventEndDateTime = itemView.findViewById(R.id.textViewBandBookingRequestItemEventEndDateTime);
            this.textViewBandBookingRequestItemViewLocationButton = itemView.findViewById(R.id.textViewBandBookingRequestItemViewLocationButton);
            this.textViewBandBookingRequestItemMessageNowButton = itemView.findViewById(R.id.textViewBandBookingRequestItemMessageNowButton);
            this.textViewBandBookingRequestItemCallNowButton = itemView.findViewById(R.id.textViewBandBookingRequestItemCallNowButton);
            this.textViewBandBookingRequestAcceptButton = itemView.findViewById(R.id.textViewBandBookingRequestAcceptButton);
            this.textViewBandBookingRequestDeclineButton = itemView.findViewById(R.id.textViewBandBookingRequestDeclineButton);
        }
    }

    ArrayList<TuneUpMeBandBookingRequestItem> tuneUpMeBandBookingRequestItemArrayList;

    public TuneUpMeBandBookingRequestItemAdapter(ArrayList<TuneUpMeBandBookingRequestItem> tuneUpMeBandBookingRequestItemArrayList) {
        this.tuneUpMeBandBookingRequestItemArrayList = tuneUpMeBandBookingRequestItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeBandBookingRequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.band_booking_request_item, parent, false);
        return new TuneUpMeBandBookingRequestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuneUpMeBandBookingRequestItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textViewBandBookingRequestItemCustomerName.setText(tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerName());
        holder.textViewBandBookingRequestItemCustomerEmail.setText(tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerEmail());
        holder.textViewBandBookingRequestItemCustomerMobile.setText(tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerMobile());
        holder.textViewBandBookingRequestItemEventStartDateTime.setText(tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerEventStartDateTime());
        holder.textViewBandBookingRequestItemEventEndDateTime.setText(tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerEventEndDateTime());


        holder.textViewBandBookingRequestItemViewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityLocationViewer.class);
                intent.putExtra("latitude", tuneUpMeBandBookingRequestItemArrayList.get(position).getLatitude());
                intent.putExtra("longitude", tuneUpMeBandBookingRequestItemArrayList.get(position).getLongitude());
                v.getContext().startActivity(intent);
            }
        });
        holder.textViewBandBookingRequestItemMessageNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityChat.class);
                intent.putExtra("user_id", tuneUpMeBandBookingRequestItemArrayList.get(position).getBandId());
                intent.putExtra("guest_id", tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerId());
                intent.putExtra("guest_type", "band");
                v.getContext().startActivity(intent);
            }
        });
        holder.textViewBandBookingRequestItemCallNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = "tel:" + tuneUpMeBandBookingRequestItemArrayList.get(position).getCustomerMobile();
                Uri uri = Uri.parse(mobile);
                intent.setData(uri);
                v.getContext().startActivity(intent);
            }
        });
        holder.textViewBandBookingRequestAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("book_req_status")
                        .whereEqualTo("status", "accepted")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                        if (documentSnapshot.exists()) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("book_req_status_id", documentSnapshot.getId());
                                            firebaseFirestore.collection("band_booking_request")
                                                    .document(tuneUpMeBandBookingRequestItemArrayList.get(position).getItemId())
                                                    .update(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toasty.success(v.getContext(), "Booking Request Accepted!", Toast.LENGTH_SHORT, true).show();
                                                            tuneUpMeBandBookingRequestItemArrayList.remove(position);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toasty.error(v.getContext(), "Oops Something went wrong!", Toast.LENGTH_SHORT, true).show();
                                                        }
                                                    });
                                        }

                                    }
                                }
                            }
                        });
            }
        });
        holder.textViewBandBookingRequestDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("book_req_status")
                        .whereEqualTo("status", "rejected")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                        if (documentSnapshot.exists()) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("book_req_status_id", documentSnapshot.getId());
                                            firebaseFirestore.collection("band_booking_request")
                                                    .document(tuneUpMeBandBookingRequestItemArrayList.get(position).getItemId())
                                                    .update(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toasty.warning(v.getContext(), "Booking Request Rejected!", Toast.LENGTH_SHORT, true).show();
                                                            tuneUpMeBandBookingRequestItemArrayList.remove(position);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toasty.error(v.getContext(), "Oops Something went wrong!", Toast.LENGTH_SHORT, true).show();
                                                        }
                                                    });
                                        }

                                    }
                                }
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return tuneUpMeBandBookingRequestItemArrayList.size();
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
