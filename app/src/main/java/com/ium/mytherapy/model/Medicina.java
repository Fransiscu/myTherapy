package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Medicina implements Parcelable {
    private String nome;
    private String descrizione;
    private String frequenza;
    private String ora;
    private String consigliSupervisore;
    private int dosaggio;
    private boolean presa;

    private Medicina(Parcel in) {
        nome = in.readString();
        descrizione = in.readString();
        frequenza = in.readString();
        ora = in.readString();
        consigliSupervisore = in.readString();
        dosaggio = in.readInt();
        presa = in.readByte() != 0;
    }

    public static Creator<Medicina> getCREATOR() {
        return CREATOR;
    }

    public String getConsigliSupervisore() {
        return consigliSupervisore;
    }

    void setConsigliSupervisore(String consigliSupervisore) {
        this.consigliSupervisore = consigliSupervisore;
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

    Medicina() {

    }

    public String getNome() {
        return nome;
    }

    void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFrequenza() {
        return frequenza;
    }

    void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public String getOra() {
        return ora;
    }

    void setOra(String ora) {
        this.ora = ora;
    }

    public int getDosaggio() {
        return dosaggio;
    }

    void setDosaggio(int dosaggio) {
        this.dosaggio = dosaggio;
    }

    public boolean isPresa() {
        return presa;
    }

    void setPresa(boolean presa) {
        this.presa = presa;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeString(descrizione);
        parcel.writeString(frequenza);
        parcel.writeString(ora);
        parcel.writeString(consigliSupervisore);
        parcel.writeInt(dosaggio);
        parcel.writeByte((byte) (presa ? 1 : 0));
    }
}
