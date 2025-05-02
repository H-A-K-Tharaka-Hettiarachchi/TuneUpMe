package com.kshprimeindustries.tuneupme.model;

public class TuneUpMeBandMyScheduleItem {


    private String itemId;
    private String customerId;
    private String bandId;
    private String customerName;
    private String customerEmail;
    private String customerMobile;
    private String customerEventStartDateTime;
    private String customerEventEndDateTime;
    private boolean paymentStatus;
    private String latitude;
    private String longitude;

    public TuneUpMeBandMyScheduleItem() {
    }

    public TuneUpMeBandMyScheduleItem(
            String itemId,
            String customerId,
            String bandId,
            String customerName,
            String customerEmail,
            String customerMobile,
            String customerEventStartDateTime,
            String customerEventEndDateTime,
            boolean paymentStatus,
            String latitude,
            String longitude
    ) {
        this.itemId = itemId;
        this.customerId = customerId;
        this.bandId = bandId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerMobile = customerMobile;
        this.customerEventStartDateTime = customerEventStartDateTime;
        this.customerEventEndDateTime = customerEventEndDateTime;
        this.paymentStatus = paymentStatus;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBandId() {
        return bandId;
    }

    public void setBandId(String bandId) {
        this.bandId = bandId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEventStartDateTime() {
        return customerEventStartDateTime;
    }

    public void setCustomerEventStartDateTime(String customerEventStartDateTime) {
        this.customerEventStartDateTime = customerEventStartDateTime;
    }

    public String getCustomerEventEndDateTime() {
        return customerEventEndDateTime;
    }

    public void setCustomerEventEndDateTime(String customerEventEndDateTime) {
        this.customerEventEndDateTime = customerEventEndDateTime;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
