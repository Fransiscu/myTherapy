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
        temp.setDescrizione("Le vitamine sono un composto organico e un nutriente essenziale che un organismo richiede in quantità limitate");
        temp.setDosaggio("Una compressa al giorno al risveglio");
        temp.setFrequenza("settimana");
        temp.setFrequenzaNum(1);
        temp.setOra("08:30");
        temp.setConsigliSupervisore("Da prendere la mattina presto a stomaco vuoto");
        temp.setLink("https://it.wikipedia.org/wiki/Vitamine");
        temp.setPresa(true);
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Folidex");
        temp.setDescrizione("L'acido folico è una vitamina che serve per il ricambio delle cellule del corpo. Lei necessita di un regolare apporto di acido folico per mantenersi in salute.");
        temp.setDosaggio("Una compressa al giorno dopo il pasto");
        temp.setFrequenza("giorno");
        temp.setFrequenzaNum(1);
        temp.setOra("12:30");
        temp.setConsigliSupervisore("Da prendere necessariamente a stomaco pieno");
        temp.setLink("https://www.my-personaltrainer.it/Foglietti-illustrativi/Folidex.html");
        temp.setPresa(false);
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Cardioaspirina");
        temp.setDescrizione("La cardioaspirina è un farmaco a base di acido acetilsalicilico ed appartiene alla famiglia dei farmaci antitrombotici.");
        temp.setDosaggio("Una compressa al giorno a stomaco pieno");
        temp.setFrequenza("giorno");
        temp.setFrequenzaNum(1);
        temp.setOra("20:30");
        temp.setConsigliSupervisore("Una compressa al giorno in un'unica somministrazione, non prima di 45 minuti dopo la fine del pasto");
        temp.setLink("https://www.my-personaltrainer.it/Foglietti-illustrativi/Cardioaspirin.html");
        temp.setPresa(false);
        list.add(temp);

        return list;

    }
}
