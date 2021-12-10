package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Medicine implements Parcelable, Comparable<Medicine> {

    private int code;
    private String name;
    private String description;
    private String frequency;
    private String timeHour;
    private String supervisorTips;
    private String dosage;
    private String link;
    private int frequencyNumber;
    private String reminder;
    private boolean delayed;
    private boolean notificationEnabled;
    private boolean taken;

    public Medicine(Parcel in) {
        code = in.readInt();
        name = in.readString();
        description = in.readString();
        frequency = in.readString();
        frequencyNumber = in.readInt();
        timeHour = in.readString();
        supervisorTips = in.readString();
        dosage = in.readString();
        link = in.readString();
        notificationEnabled = in.readByte() != 0;
        taken = in.readByte() != 0;
        delayed = in.readByte() != 0;
        reminder = in.readString();
    }

    public Medicine medicine;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(frequency);
        parcel.writeInt(frequencyNumber);
        parcel.writeString(timeHour);
        parcel.writeString(supervisorTips);
        parcel.writeString(dosage);
        parcel.writeString(link);
        parcel.writeByte((byte) (notificationEnabled ? 1 : 0));
        parcel.writeByte((byte) (taken ? 1 : 0));
        parcel.writeByte((byte) (delayed ? 1 : 0));
        parcel.writeString(reminder);
    }

    public Medicine getMedicina() {
        return medicine;
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public void setMedicina(Medicine medicine) {
        this.medicine = medicine;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public Medicine() {

    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    int getCode() {
        return code;
    }

    void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(int frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    public String getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(String timeHour) {
        this.timeHour = timeHour;
    }

    public String getSupervisorTips() {
        return supervisorTips;
    }

    public void setSupervisorTips(String supervisorTips) {
        this.supervisorTips = supervisorTips;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return code + " " + name;
    }

    @Override
    public int compareTo(Medicine medicine) {
        String oraEdited = timeHour.replace(":", "");    // workaround per confrontare le ore
        String toOraEdited = medicine.timeHour.replace(":", "");
        if (oraEdited.equals(toOraEdited)) {
            return 0;
        } else if (Integer.parseInt(oraEdited) > Integer.parseInt(toOraEdited)) {
            return 1;
        } else {
            return -1;
        }
    }
}
