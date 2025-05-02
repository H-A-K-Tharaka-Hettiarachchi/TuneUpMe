package com.kshprimeindustries.tuneupme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.model.TuneUpMeSelectBandTypeItem;

import java.util.List;

public class TuneUpMeSelectBandTypeItemAdapter extends ArrayAdapter<TuneUpMeSelectBandTypeItem> {

    List<TuneUpMeSelectBandTypeItem> tuneUpMeSelectBandTypeItemList;

    int spinner_select_band_type_item;

    public TuneUpMeSelectBandTypeItemAdapter(@NonNull Context context, int spinner_select_band_type_item, @NonNull List<TuneUpMeSelectBandTypeItem> tuneUpMeSelectBandTypeItemList) {
        super(context, spinner_select_band_type_item, tuneUpMeSelectBandTypeItemList);
        this.tuneUpMeSelectBandTypeItemList = tuneUpMeSelectBandTypeItemList;
        this.spinner_select_band_type_item = spinner_select_band_type_item;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(spinner_select_band_type_item, parent, false);

        TextView textViewSpinnerSelectBandTypeItem = view.findViewById(R.id.textViewSpinnerSelectBandTypeItem);

        TuneUpMeSelectBandTypeItem tuneUpMeSelectBandTypeItem = tuneUpMeSelectBandTypeItemList.get(position);

        textViewSpinnerSelectBandTypeItem.setText(tuneUpMeSelectBandTypeItem.getType());

        return view;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.spinner_select_band_type_view, parent, false);

        TextView textViewSpinnerSelectBandTypeView = view.findViewById(R.id.textViewSpinnerSelectBandTypeView);

        TuneUpMeSelectBandTypeItem tuneUpMeSelectBandTypeItem = tuneUpMeSelectBandTypeItemList.get(position);

        textViewSpinnerSelectBandTypeView.setText(tuneUpMeSelectBandTypeItem.getType());

        return view;
    }
}
