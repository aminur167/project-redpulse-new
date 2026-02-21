package com.example.projectredpulsenew;

public class NotificationDetails {

    private String clickerName;
    private String clickerBloodgroup;
    private String clickerDistrict;
    private String clickerNumber;
    private String clickerEmail;
    private String clickerPassword;
    private String clickerProfilePic;
    private String receiverEmail;
    private String receiverPassword;
    private String notiType;

    // 🔴 NEW FIELD - eta add koro
    private long timestamp;  // notification create er time store korbe

    // No-argument constructor
    public NotificationDetails() {
        // 🔴 NEW - default timestamp set koro
        this.timestamp = System.currentTimeMillis();
    }

    // Parameterized constructor (update koro)
    public NotificationDetails(String clickerName, String clickerBloodgroup, String clickerDistrict, String clickerNumber,
                               String clickerEmail, String clickerPassword, String clickerProfilePic,
                               String receiverEmail, String receiverPassword,
                               String notiType) {
        this.clickerName = clickerName;
        this.clickerBloodgroup = clickerBloodgroup;
        this.clickerDistrict = clickerDistrict;
        this.clickerNumber = clickerNumber;
        this.clickerEmail = clickerEmail;
        this.clickerPassword = clickerPassword;
        this.clickerProfilePic = clickerProfilePic;
        this.receiverEmail = receiverEmail;
        this.receiverPassword = receiverPassword;
        this.notiType = notiType;

        // 🔴 NEW
        this.timestamp = System.currentTimeMillis();
    }

    // =================== Getters ===================
    public String getClickerName() { return clickerName; }
    public String getClickerBloodgroup() { return clickerBloodgroup; }
    public String getClickerDistrict() { return clickerDistrict; }
    public String getClickerNumber() { return clickerNumber; }
    public String getClickerEmail() { return clickerEmail; }
    public String getClickerPassword() { return clickerPassword; }
    public String getClickerProfilePic() { return clickerProfilePic; }
    public String getReceiverEmail() { return receiverEmail; }
    public String getReceiverPassword() { return receiverPassword; }
    public String getNotiType() { return notiType; }

    // 🔴 NEW Getter/Setter for timestamp
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // =================== Setters ===================
    public void setClickerName(String clickerName) { this.clickerName = clickerName; }
    public void setClickerBloodgroup(String clickerBloodgroup) { this.clickerBloodgroup = clickerBloodgroup; }
    public void setClickerDistrict(String clickerDistrict) { this.clickerDistrict = clickerDistrict; }
    public void setClickerNumber(String clickerNumber) { this.clickerNumber = clickerNumber; }
    public void setClickerEmail(String clickerEmail) { this.clickerEmail = clickerEmail; }
    public void setClickerPassword(String clickerPassword) { this.clickerPassword = clickerPassword; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
    public void setReceiverPassword(String receiverPassword) { this.receiverPassword = receiverPassword; }
    public void setNotiType(String notiType) { this.notiType = notiType; }
    public void setClickerPic(String clickerProfilePic) { this.clickerProfilePic = clickerProfilePic; }
}