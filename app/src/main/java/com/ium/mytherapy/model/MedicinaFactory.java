package com.ium.mytherapy.model;

import java.util.ArrayList;
import java.util.List;

public class MedicinaFactory {

    private static MedicinaFactory dummy;

    private MedicinaFactory() {
    }

    public static MedicinaFactory getInstance() {
        if (dummy == null) {
            dummy = new MedicinaFactory();
        }
        return dummy;
    }

    /* Medicine d'esempio */
    public static List<Medicina> getMedicines() {
        List<Medicina> list = new ArrayList<>();

        Medicina temp = new Medicina();
        temp.setNome("Vitamine");
        temp.setDescrizione("Le vitamine fanno bene");
        temp.setDosaggio(1);
        temp.setFrequenza("week");
        temp.setOra("08:30");
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Folidex");
        temp.setDescrizione("Acido folico");
        temp.setDosaggio(1);
        temp.setFrequenza("day");
        temp.setOra("12:30");
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Cardioaspirina");
        temp.setDescrizione("Aspirina");
        temp.setDosaggio(1);
        temp.setFrequenza("day");
        temp.setOra("20:30");
        list.add(temp);

        return list;

    }
}
