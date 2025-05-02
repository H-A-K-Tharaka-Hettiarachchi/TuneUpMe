package com.kshprimeindustries.tuneupme.model;

public class TuneUpMeCustomerRequestedBandPartnershipItem {

    private String id;
    private String name;
    private String bandType;
    private String email;
    private String mobile;
    private String bandReqStatus;
    private String customerId;

    public TuneUpMeCustomerRequestedBandPartnershipItem() {
    }


    public TuneUpMeCustomerRequestedBandPartnershipItem(String id, String name, String bandType, String email, String mobile, String bandReqStatus, String customerId) {
        this.id = id;
        this.name = name;
        this.bandType = bandType;
        this.email = email;
        this.mobile = mobile;
        this.bandReqStatus = bandReqStatus;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBandType() {
        return bandType;
    }

    public void setBandType(String bandType) {
        this.bandType = bandType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBandReqStatus() {
        return bandReqStatus;
    }

    public void setBandReqStatus(String bandReqStatus) {
        this.bandReqStatus = bandReqStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
