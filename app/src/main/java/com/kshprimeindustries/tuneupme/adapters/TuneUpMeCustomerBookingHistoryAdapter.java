package com.kshprimeindustries.tuneupme.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityLocationViewer;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerBookingHistoryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.model.InitRequest;


public class TuneUpMeCustomerBookingHistoryAdapter extends RecyclerView.Adapter<TuneUpMeCustomerBookingHistoryAdapter.TuneUpMeCustomerBookingHistoryViewHolder> {


    public static int PAYHERE_REQUEST = 123456;
    public static String itemId;

    Activity activity;

    static class TuneUpMeCustomerBookingHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCustomerBookingHistoryBandName;
        TextView textViewCustomerBookingHistoryStartDateTime;
        TextView textViewCustomerBookingHistoryEndDateTime;
        TextView textViewCustomerBookingHistoryStatus;
        TextView textViewCustomerBookingHistoryPayChargesButton;

        ImageView imageViewCustomerBookingHistoryViewLocation;

        public TuneUpMeCustomerBookingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewCustomerBookingHistoryBandName = itemView.findViewById(R.id.textViewCustomerBookingHistoryBandName);
            this.textViewCustomerBookingHistoryStartDateTime = itemView.findViewById(R.id.textViewCustomerBookingHistoryStartDateTime);
            this.textViewCustomerBookingHistoryEndDateTime = itemView.findViewById(R.id.textViewCustomerBookingHistoryEndDateTime);
            this.textViewCustomerBookingHistoryStatus = itemView.findViewById(R.id.textViewCustomerBookingHistoryStatus);
            this.textViewCustomerBookingHistoryPayChargesButton = itemView.findViewById(R.id.textViewCustomerBookingHistoryPayChargesButton);
            this.imageViewCustomerBookingHistoryViewLocation = itemView.findViewById(R.id.imageViewCustomerBookingHistoryViewLocation);
        }
    }

    ArrayList<TuneUpMeCustomerBookingHistoryItem> tuneUpMeCustomerBookingHistoryItemArrayList;

    public TuneUpMeCustomerBookingHistoryAdapter(ArrayList<TuneUpMeCustomerBookingHistoryItem> tuneUpMeCustomerBookingHistoryItemArrayList) {

        this.tuneUpMeCustomerBookingHistoryItemArrayList = tuneUpMeCustomerBookingHistoryItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeCustomerBookingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.customer_booking_history_item, parent, false);

        return new TuneUpMeCustomerBookingHistoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TuneUpMeCustomerBookingHistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        activity = (Activity) holder.itemView.getContext();
        holder.textViewCustomerBookingHistoryBandName.setText(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryBandName());
        holder.textViewCustomerBookingHistoryStartDateTime.setText(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStartDateTime());
        holder.textViewCustomerBookingHistoryEndDateTime.setText(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryEndDateTime());


        if (tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStatus().equals("pending")) {
            holder.textViewCustomerBookingHistoryStatus.setText("Pending");
            holder.textViewCustomerBookingHistoryPayChargesButton.setVisibility(View.GONE);
        } else if (tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStatus().equals("rejected")) {
            holder.textViewCustomerBookingHistoryStatus.setText("Rejected");
            holder.textViewCustomerBookingHistoryPayChargesButton.setVisibility(View.GONE);
        } else if (tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStatus().equals("accepted")) {
            holder.textViewCustomerBookingHistoryStatus.setText("Accepted");
        } else if (tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStatus().equals("booked")) {
            holder.textViewCustomerBookingHistoryStatus.setText("Booked");
            holder.textViewCustomerBookingHistoryPayChargesButton.setVisibility(View.GONE);
        }

        holder.imageViewCustomerBookingHistoryViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityLocationViewer.class);
                intent.putExtra("latitude", tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryLatitude());
                intent.putExtra("longitude", tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryLongitude());
                v.getContext().startActivity(intent);

            }
        });
        holder.textViewCustomerBookingHistoryPayChargesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("band")
                        .document(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getBandId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {

                                        SharedPreferences sharedPreferences = activity.getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
                                        String customer_id = sharedPreferences.getString("id", null);

                                        firebaseFirestore.collection("customer")
                                                .document(customer_id)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {

                                                                double amount = calculateFinalPrice(
                                                                        tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryStartDateTime(),
                                                                        tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryEndDateTime(),
                                                                        Double.parseDouble(Objects.requireNonNull(documentSnapshot.getString("price_per_hour")))
                                                                );

                                                                String[] names = extractName(document.getString("name"));
                                                                System.out.println("First Name: " + names[0]);
                                                                System.out.println("Last Name: " + names[1]);

                                                                System.out.println("Amount " + amount);


                                                                InitRequest req = new InitRequest();
                                                                req.setMerchantId("1221052");       // Merchant ID
                                                                req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                                                                req.setAmount(amount);             // Final Amount to be charged
                                                                req.setOrderId(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getItemId());        // Unique Reference ID
                                                                req.setItemsDescription(tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getCustomerBookingHistoryBandName());  // Item description title
                                                                req.setCustom1("This is the custom message 1");
                                                                req.setCustom2("This is the custom message 2");
                                                                req.getCustomer().setFirstName(names[0]);
                                                                req.getCustomer().setLastName(names[1]);
                                                                req.getCustomer().setEmail(document.getString("email"));
                                                                req.getCustomer().setPhone(document.getString("mobile"));
                                                                req.getCustomer().getAddress().setAddress("");
                                                                req.getCustomer().getAddress().setCity("");
                                                                req.getCustomer().getAddress().setCountry("Sri Lanka");


                                                                Intent intent = new Intent(activity, PHMainActivity.class);
                                                                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                                                                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                                                                activity.startActivityForResult(intent, PAYHERE_REQUEST);
                                                                itemId = tuneUpMeCustomerBookingHistoryItemArrayList.get(position).getItemId();

                                                            }
                                                        }
                                                    }
                                                });


                                    }
                                }
                            }
                        });


            }
        });

    }

    public static double calculateFinalPrice(String startTimeStr, String endTimeStr, double hourlyRate) {
        // Define formatter to parse your date-time format
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-hh:mm a");

        try {
            // Parse start and end time to Date objects
            Date startDateTime = formatter.parse(startTimeStr);
            Date endDateTime = formatter.parse(endTimeStr);

            // Calculate duration in minutes
            assert startDateTime != null;
            assert endDateTime != null;
            long durationInMillis = endDateTime.getTime() - startDateTime.getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);

            // Convert minutes to fractional hours
            double hours = minutes / 60.0;

            // Calculate final price
            return hours * hourlyRate;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0.0; // Return 0 if parsing fails
        }
    }

    public static String[] extractName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", ""}; // Return empty strings if input is null or empty
        }

        String[] parts = fullName.trim().split("\\s+"); // Split by spaces

        String fname = parts[0]; // First word as first name
        String lname = (parts.length > 1) ? parts[parts.length - 1] : ""; // Last word as last name

        return new String[]{fname, lname};
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

    @Override
    public int getItemCount() {
        return tuneUpMeCustomerBookingHistoryItemArrayList.size();
    }


}
