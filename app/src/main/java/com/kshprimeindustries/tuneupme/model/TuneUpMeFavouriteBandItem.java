package com.kshprimeindustries.tuneupme.model;

import android.graphics.drawable.Drawable;

public class TuneUpMeFavouriteBandItem {

    private String TuneUpMeFavouriteBandItemId;
    private Drawable TuneUpMeFavouriteBandItemProfilePicture;
    private String TuneUpMeFavouriteBandItemProfilePictureSavedPath;
    private String TuneUpMeFavouriteBandItemBandName;
    private boolean TuneUpMeFavouriteBandItemFav;

    public TuneUpMeFavouriteBandItem() {
    }

    public TuneUpMeFavouriteBandItem(String tuneUpMeFavouriteBandItemId, Drawable tuneUpMeFavouriteBandItemProfilePicture,String TuneUpMeFavouriteBandItemProfilePictureSavedPath, String tuneUpMeFavouriteBandItemBandName, boolean TuneUpMeFavouriteBandItemFav) {
        this.TuneUpMeFavouriteBandItemId = tuneUpMeFavouriteBandItemId;
        this.TuneUpMeFavouriteBandItemProfilePicture = tuneUpMeFavouriteBandItemProfilePicture;
        this.TuneUpMeFavouriteBandItemProfilePictureSavedPath = TuneUpMeFavouriteBandItemProfilePictureSavedPath;
        this.TuneUpMeFavouriteBandItemBandName = tuneUpMeFavouriteBandItemBandName;
        this.TuneUpMeFavouriteBandItemFav = TuneUpMeFavouriteBandItemFav;
    }


    public String getTuneUpMeFavouriteBandItemId() {
        return TuneUpMeFavouriteBandItemId;
    }

    public void setTuneUpMeFavouriteBandItemId(String tuneUpMeFavouriteBandItemId) {
        TuneUpMeFavouriteBandItemId = tuneUpMeFavouriteBandItemId;
    }

    public Drawable getTuneUpMeFavouriteBandItemProfilePicture() {
        return TuneUpMeFavouriteBandItemProfilePicture;
    }

    public String getTuneUpMeFavouriteBandItemProfilePictureSavedPath() {
        return TuneUpMeFavouriteBandItemProfilePictureSavedPath;
    }

    public void setTuneUpMeFavouriteBandItemProfilePictureSavedPath(String tuneUpMeFavouriteBandItemProfilePictureSavedPath) {
        TuneUpMeFavouriteBandItemProfilePictureSavedPath = tuneUpMeFavouriteBandItemProfilePictureSavedPath;
    }

    public void setTuneUpMeFavouriteBandItemProfilePicture(Drawable tuneUpMeFavouriteBandItemProfilePicture) {
        TuneUpMeFavouriteBandItemProfilePicture = tuneUpMeFavouriteBandItemProfilePicture;
    }

    public String getTuneUpMeFavouriteBandItemBandName() {
        return TuneUpMeFavouriteBandItemBandName;
    }

    public void setTuneUpMeFavouriteBandItemBandName(String tuneUpMeFavouriteBandItemBandName) {
        TuneUpMeFavouriteBandItemBandName = tuneUpMeFavouriteBandItemBandName;
    }

    public boolean isTuneUpMeFavouriteBandItemFav() {
        return TuneUpMeFavouriteBandItemFav;
    }

    public void setTuneUpMeFavouriteBandItemFav(boolean tuneUpMeFavouriteBandItemFav) {
        TuneUpMeFavouriteBandItemFav = tuneUpMeFavouriteBandItemFav;
    }


}
