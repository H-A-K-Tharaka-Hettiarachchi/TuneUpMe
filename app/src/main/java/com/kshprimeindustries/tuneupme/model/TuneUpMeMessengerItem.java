package com.kshprimeindustries.tuneupme.model;

public class TuneUpMeMessengerItem {


    private String TuneUpMeMessengerItemId;
    private String TuneUpMeMessengerItemProfileName;
    private String TuneUpMeMessengerItemMessage;
    private String TuneUpMeMessengerItemMessageCount;
    private String TuneUpMeMessengerItemMessageTime;
    private String TuneUpMeMessengerItemProfilePicture;
    private boolean TuneUpMeMessengerItemMessageStatus;

    public TuneUpMeMessengerItem() {
    }

    public TuneUpMeMessengerItem(String tuneUpMeMessengerItemId, String tuneUpMeMessengerItemProfileName, String tuneUpMeMessengerItemMessage, String tuneUpMeMessengerItemMessageCount, String tuneUpMeMessengerItemMessageTime, String tuneUpMeMessengerItemProfilePicture, boolean tuneUpMeMessengerItemMessageStatus) {
        TuneUpMeMessengerItemId = tuneUpMeMessengerItemId;
        TuneUpMeMessengerItemProfileName = tuneUpMeMessengerItemProfileName;
        TuneUpMeMessengerItemMessage = tuneUpMeMessengerItemMessage;
        TuneUpMeMessengerItemMessageCount = tuneUpMeMessengerItemMessageCount;
        TuneUpMeMessengerItemMessageTime = tuneUpMeMessengerItemMessageTime;
        TuneUpMeMessengerItemProfilePicture = tuneUpMeMessengerItemProfilePicture;
        TuneUpMeMessengerItemMessageStatus = tuneUpMeMessengerItemMessageStatus;
    }

    public String getTuneUpMeMessengerItemId() {
        return TuneUpMeMessengerItemId;
    }

    public void setTuneUpMeMessengerItemId(String tuneUpMeMessengerItemId) {
        TuneUpMeMessengerItemId = tuneUpMeMessengerItemId;
    }

    public String getTuneUpMeMessengerItemProfileName() {
        return TuneUpMeMessengerItemProfileName;
    }

    public void setTuneUpMeMessengerItemProfileName(String tuneUpMeMessengerItemProfileName) {
        TuneUpMeMessengerItemProfileName = tuneUpMeMessengerItemProfileName;
    }

    public String getTuneUpMeMessengerItemMessage() {
        return TuneUpMeMessengerItemMessage;
    }

    public void setTuneUpMeMessengerItemMessage(String tuneUpMeMessengerItemMessage) {
        TuneUpMeMessengerItemMessage = tuneUpMeMessengerItemMessage;
    }

    public String getTuneUpMeMessengerItemMessageCount() {
        return TuneUpMeMessengerItemMessageCount;
    }

    public void setTuneUpMeMessengerItemMessageCount(String tuneUpMeMessengerItemMessageCount) {
        TuneUpMeMessengerItemMessageCount = tuneUpMeMessengerItemMessageCount;
    }

    public String getTuneUpMeMessengerItemMessageTime() {
        return TuneUpMeMessengerItemMessageTime;
    }

    public void setTuneUpMeMessengerItemMessageTime(String tuneUpMeMessengerItemMessageTime) {
        TuneUpMeMessengerItemMessageTime = tuneUpMeMessengerItemMessageTime;
    }

    public String getTuneUpMeMessengerItemProfilePicture() {
        return TuneUpMeMessengerItemProfilePicture;
    }

    public void setTuneUpMeMessengerItemProfilePicture(String tuneUpMeMessengerItemProfilePicture) {
        TuneUpMeMessengerItemProfilePicture = tuneUpMeMessengerItemProfilePicture;
    }

    public boolean isTuneUpMeMessengerItemMessageStatus() {
        return TuneUpMeMessengerItemMessageStatus;
    }

    public void setTuneUpMeMessengerItemMessageStatus(boolean tuneUpMeMessengerItemMessageStatus) {
        TuneUpMeMessengerItemMessageStatus = tuneUpMeMessengerItemMessageStatus;
    }
}
