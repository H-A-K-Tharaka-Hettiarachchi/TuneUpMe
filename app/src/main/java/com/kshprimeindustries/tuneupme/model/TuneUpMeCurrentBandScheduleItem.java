package com.kshprimeindustries.tuneupme.model;

import android.widget.TextView;

public class TuneUpMeCurrentBandScheduleItem {

    private String currentBandScheduleBandName;
    private String currentBandScheduleStartDateTime;
    private String currentBandScheduleEndDateTime;
    private boolean currentBandScheduleBookingConfirmed;


    public TuneUpMeCurrentBandScheduleItem() {
    }

    public TuneUpMeCurrentBandScheduleItem(String currentBandScheduleBandName, String currentBandScheduleStartDateTime, String currentBandScheduleEndDateTime, boolean currentBandScheduleBookingConfirmed) {
        this.currentBandScheduleBandName = currentBandScheduleBandName;
        this.currentBandScheduleStartDateTime = currentBandScheduleStartDateTime;
        this.currentBandScheduleEndDateTime = currentBandScheduleEndDateTime;
        this.currentBandScheduleBookingConfirmed = currentBandScheduleBookingConfirmed;
    }

    public String getCurrentBandScheduleBandName() {
        return currentBandScheduleBandName;
    }

    public void setCurrentBandScheduleBandName(String currentBandScheduleBandName) {
        this.currentBandScheduleBandName = currentBandScheduleBandName;
    }

    public String getCurrentBandScheduleStartDateTime() {
        return currentBandScheduleStartDateTime;
    }

    public void setCurrentBandScheduleStartDateTime(String currentBandScheduleStartDateTime) {
        this.currentBandScheduleStartDateTime = currentBandScheduleStartDateTime;
    }

    public String getCurrentBandScheduleEndDateTime() {
        return currentBandScheduleEndDateTime;
    }

    public void setCurrentBandScheduleEndDateTime(String currentBandScheduleEndDateTime) {
        this.currentBandScheduleEndDateTime = currentBandScheduleEndDateTime;
    }

    public boolean isCurrentBandScheduleBookingConfirmed() {
        return currentBandScheduleBookingConfirmed;
    }

    public void setCurrentBandScheduleBookingConfirmed(boolean currentBandScheduleBookingConfirmed) {
        this.currentBandScheduleBookingConfirmed = currentBandScheduleBookingConfirmed;
    }
}
