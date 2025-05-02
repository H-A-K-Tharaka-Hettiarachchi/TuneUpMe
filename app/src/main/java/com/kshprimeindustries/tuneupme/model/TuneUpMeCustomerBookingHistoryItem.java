package com.kshprimeindustries.tuneupme.model;

import android.widget.TextView;

public class TuneUpMeCustomerBookingHistoryItem {

    private String itemId;
    private String bandId;
    private String customerBookingHistoryBandName;
    private String customerBookingHistoryStartDateTime;
    private String customerBookingHistoryEndDateTime;
    private String customerBookingHistoryStatus;
    private String customerBookingHistoryLatitude;
    private String customerBookingHistoryLongitude;


    public TuneUpMeCustomerBookingHistoryItem() {
    }

    public TuneUpMeCustomerBookingHistoryItem(
            String itemId,
            String bandId,
            String customerBookingHistoryBandName,
            String customerBookingHistoryStartDateTime,
            String customerBookingHistoryEndDateTime,
            String customerBookingHistoryStatus,
            String customerBookingHistoryLatitude,
            String customerBookingHistoryLongitude
    ) {
        this.itemId = itemId;
        this.bandId = bandId;
        this.customerBookingHistoryBandName = customerBookingHistoryBandName;
        this.customerBookingHistoryStartDateTime = customerBookingHistoryStartDateTime;
        this.customerBookingHistoryEndDateTime = customerBookingHistoryEndDateTime;
        this.customerBookingHistoryStatus = customerBookingHistoryStatus;
        this.customerBookingHistoryLatitude = customerBookingHistoryLatitude;
        this.customerBookingHistoryLongitude = customerBookingHistoryLongitude;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBandId() {
        return bandId;
    }

    public void setBandId(String bandId) {
        this.bandId = bandId;
    }

    public String getCustomerBookingHistoryBandName() {
        return customerBookingHistoryBandName;
    }

    public void setCustomerBookingHistoryBandName(String customerBookingHistoryBandName) {
        this.customerBookingHistoryBandName = customerBookingHistoryBandName;
    }

    public String getCustomerBookingHistoryStartDateTime() {
        return customerBookingHistoryStartDateTime;
    }

    public void setCustomerBookingHistoryStartDateTime(String customerBookingHistoryStartDateTime) {
        this.customerBookingHistoryStartDateTime = customerBookingHistoryStartDateTime;
    }

    public String getCustomerBookingHistoryEndDateTime() {
        return customerBookingHistoryEndDateTime;
    }

    public void setCustomerBookingHistoryEndDateTime(String customerBookingHistoryEndDateTime) {
        this.customerBookingHistoryEndDateTime = customerBookingHistoryEndDateTime;
    }

    public String getCustomerBookingHistoryStatus() {
        return customerBookingHistoryStatus;
    }

    public void setCustomerBookingHistoryStatus(String customerBookingHistoryStatus) {
        this.customerBookingHistoryStatus = customerBookingHistoryStatus;
    }

    public String getCustomerBookingHistoryLatitude() {
        return customerBookingHistoryLatitude;
    }

    public void setCustomerBookingHistoryLatitude(String customerBookingHistoryLatitude) {
        this.customerBookingHistoryLatitude = customerBookingHistoryLatitude;
    }

    public String getCustomerBookingHistoryLongitude() {
        return customerBookingHistoryLongitude;
    }

    public void setCustomerBookingHistoryLongitude(String customerBookingHistoryLongitude) {
        this.customerBookingHistoryLongitude = customerBookingHistoryLongitude;
    }
}
