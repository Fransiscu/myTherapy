package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Medicina implements Parcelable, Comparable<Medicina> {

    private int code;
    private String nome;
    private String descrizione;
    private String frequenza;
    private int frequenzaNum;
    private String ora;
    private String consigliSupervisore;
    private String dosaggio;
    private String link;
    private boolean notifEnabled;
    private boolean presa;

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

    protected Medicina(Parcel in) {
        code = in.readInt();
        nome = in.readString();
        descrizione = in.readString();
        frequenza = in.readString();
        frequenzaNum = in.readInt();
        ora = in.readString();
        consigliSupervisore = in.readString();
        dosaggio = in.readString();
        link = in.readString();
        notifEnabled = in.readInt() == 0;
        presa = in.readByte() != 0;
    }

    public Medicina() {
    }

    public static Creator<Medicina> getCREATOR() {
        return CREATOR;
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
        parcel.writeInt(notifEnabled ? 1 : 0);
        parcel.writeByte((byte) (presa ? 1 : 0));
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

    int getCode() {
        return code;
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

    void setCode(int code) {
        this.code = code;
    }

    public boolean isNotifEnabled() {
        return notifEnabled;
    }

    public boolean isPresa() {
        return presa;
    }

    public void setPresa(boolean presa) {
        this.presa = presa;
    }

    public void setNotifEnabled(boolean notifEnabled) {
        this.notifEnabled = notifEnabled;
    }
}
