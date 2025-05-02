package com.kshprimeindustries.tuneupme.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class TuneUpMeCustomerHomeItem {

    private String customerHomeItemProfileId;
    private Drawable customerHomeItemProfilePicture;
    private String customerHomeItemProfilePictureSavedPath;
    private String customerHomeItemProfileName;
    private String customerHomeItemPricePerHour;
    private boolean customerHomeItemFav;


    public TuneUpMeCustomerHomeItem() {
    }


    public TuneUpMeCustomerHomeItem(String customerHomeItemProfileId, Drawable customerHomeItemProfilePicture, String customerHomeItemProfilePictureSavedPath, String customerHomeItemProfileName, String customerHomeItemPricePerHour, boolean customerHomeItemFav) {
        this.customerHomeItemProfileId = customerHomeItemProfileId;
        this.customerHomeItemProfilePicture = customerHomeItemProfilePicture;
        this.customerHomeItemProfilePictureSavedPath = customerHomeItemProfilePictureSavedPath;
        this.customerHomeItemProfileName = customerHomeItemProfileName;
        this.customerHomeItemPricePerHour = customerHomeItemPricePerHour;
        this.customerHomeItemFav = customerHomeItemFav;
    }

    public String getCustomerHomeItemProfileId() {
        return customerHomeItemProfileId;
    }

    public void setCustomerHomeItemProfileId(String customerHomeItemProfileId) {
        this.customerHomeItemProfileId = customerHomeItemProfileId;
    }


    public Drawable getCustomerHomeItemProfilePicture() {
        return customerHomeItemProfilePicture;
    }

    public void setCustomerHomeItemProfilePicture(Drawable customerHomeItemProfilePicture) {
        this.customerHomeItemProfilePicture = customerHomeItemProfilePicture;
    }

    public String getCustomerHomeItemProfilePictureSavedPath() {
        return customerHomeItemProfilePictureSavedPath;
    }

    public void setCustomerHomeItemProfilePictureSavedPath(String customerHomeItemProfilePictureSavedPath) {
        this.customerHomeItemProfilePictureSavedPath = customerHomeItemProfilePictureSavedPath;
    }

    public String getCustomerHomeItemProfileName() {
        return customerHomeItemProfileName;
    }

    public void setCustomerHomeItemProfileName(String customerHomeItemProfileName) {
        this.customerHomeItemProfileName = customerHomeItemProfileName;
    }

    public String getCustomerHomeItemPricePerHour() {
        return customerHomeItemPricePerHour;
    }

    public void setCustomerHomeItemPricePerHour(String customerHomeItemPricePerHour) {
        this.customerHomeItemPricePerHour = customerHomeItemPricePerHour;
    }

    public boolean isCustomerHomeItemFav() {
        return customerHomeItemFav;
    }

    public void setCustomerHomeItemFav(boolean customerHomeItemFav) {
        this.customerHomeItemFav = customerHomeItemFav;
    }
}
