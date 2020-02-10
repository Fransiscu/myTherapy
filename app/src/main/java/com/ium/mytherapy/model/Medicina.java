package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Medicina implements Parcelable {
    private String nome;
    private String descrizione;
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
    private String frequenza;
    private String ora;
    private int dosaggio;

    private Medicina(Parcel in) {
        nome = in.readString();
        descrizione = in.readString();
        frequenza = in.readString();
        ora = in.readString();
        dosaggio = in.readInt();
    }

    Medicina() {

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
        parcel.writeInt(dosaggio);
    }
}
