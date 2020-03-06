package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Medicina implements Parcelable, Comparable<Medicina> {

    private int code;
    private String nome;
    private String descrizione;
    private String frequenza;
    private String ora;
    private String consigliSupervisore;
    private String dosaggio;
    private String link;
    private int frequenzaNum;
    private String reminder;
    private boolean delayed;
    private boolean notifEnabled;
    private boolean presa;

    public Medicina(Parcel in) {
        code = in.readInt();
        nome = in.readString();
        descrizione = in.readString();
        frequenza = in.readString();
        frequenzaNum = in.readInt();
        ora = in.readString();
        consigliSupervisore = in.readString();
        dosaggio = in.readString();
        link = in.readString();
        notifEnabled = in.readByte() != 0;
        presa = in.readByte() != 0;
        delayed = in.readByte() != 0;
        reminder = in.readString();
    }

    public static Creator<Medicina> getCREATOR() {
        return CREATOR;
    }

    public Medicina getMedicina() {
        return medicina;
    }

    public static final Creator<Medicina> CREATOR = new Creator<Medicina>() {
        @Override
        public Medicina createFromParcel(Parcel in) {
            return new Medicina(in);
        }

        @Override
        public Medicina[] newArray(int size) {
            return new Medicina[size];
        }
    };

    public Medicina medicina;

    public void setMedicina(Medicina medicina) {
        this.medicina = medicina;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public Medicina() {

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public int getFrequenzaNum() {
        return frequenzaNum;
    }

    public void setFrequenzaNum(int frequenzaNum) {
        this.frequenzaNum = frequenzaNum;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getConsigliSupervisore() {
        return consigliSupervisore;
    }

    public void setConsigliSupervisore(String consigliSupervisore) {
        this.consigliSupervisore = consigliSupervisore;
    }

    public String getDosaggio() {
        return dosaggio;
    }

    public void setDosaggio(String dosaggio) {
        this.dosaggio = dosaggio;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isNotifEnabled() {
        return notifEnabled;
    }

    public void setNotifEnabled(boolean notifEnabled) {
        this.notifEnabled = notifEnabled;
    }

    public boolean isPresa() {
        return presa;
    }

    public void setPresa(boolean presa) {
        this.presa = presa;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(nome);
        parcel.writeString(descrizione);
        parcel.writeString(frequenza);
        parcel.writeInt(frequenzaNum);
        parcel.writeString(ora);
        parcel.writeString(consigliSupervisore);
        parcel.writeString(dosaggio);
        parcel.writeString(link);
        parcel.writeByte((byte) (notifEnabled ? 1 : 0));
        parcel.writeByte((byte) (presa ? 1 : 0));
        parcel.writeByte((byte) (delayed ? 1 : 0));
        parcel.writeString(reminder);
    }

    @NonNull
    @Override
    public String toString() {
        return code + " " + nome;
    }

    @Override
    public int compareTo(Medicina medicina) {
        String oraEdited = ora.replace(":", "");    // workaround per confrontare le ore
        String toOraEdited = medicina.ora.replace(":", "");
        if (oraEdited.equals(toOraEdited)) {
            return 0;
        } else if (Integer.parseInt(oraEdited) > Integer.parseInt(toOraEdited)) {
            return 1;
        } else {
            return -1;
        }
    }
}
