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

    /* Medicine d'esempio hardcoded */
    public List<Medicina> getMedicines() {
        List<Medicina> list = new ArrayList<>();

        Medicina temp = new Medicina();
        temp.setNome("Ketoprofene");
        temp.setDescrizione("Il ketoprofene è un farmaco antinfiammatorio non steroideo (FANS), derivato dell'acido propionico" +
                " e simile all'ibuprofene, con azione analgesica, antipiretica e di antiaggregazione piastrinica. È usato nel " +
                "trattamento di artriti reumatoidi e osteoartriti. La molecola è spesso commercializzata sotto forma di sale di lisina, " +
                "chimicamente più stabile e più facilmente conservabile nel tempo.");
        temp.setDosaggio("Adulti: una bustina da 80 mg (dose intera) tre volte al giorno durante i pasti. Bambini di età tra i 6 ed i 14 anni:" +
                " mezza bustina da 40 mg (mezza dose) tre volte al giorno durante i pasti.");
        temp.setFrequenza("Giorno");
        temp.setFrequenzaNum(1);
        temp.setOra("08:30");
        temp.setConsigliSupervisore("Da prendere a stomaco pieno dopo i pasti. Iniziare con un dosaggio di 3 al giorno e diminuire gradualmente fino alla" +
                "fine della terapia");
        temp.setLink("https://www.my-personaltrainer.it/salute-benessere/ketoprofene.html");
        temp.setPresa(true);
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Folidex");
        temp.setDescrizione("L'acido folico è una vitamina che serve per il ricambio delle cellule del corpo. Lei necessita di un " +
                "regolare apporto di acido folico per mantenersi in salute.");
        temp.setDosaggio("Una compressa al giorno dopo il pasto. La posologia giornaliera può essere raddoppiata, " +
                "in caso di inadeguato apporto di folati.");
        temp.setFrequenza("Giorno");
        temp.setFrequenzaNum(1);
        temp.setOra("12:30");
        temp.setConsigliSupervisore("Da prendere necessariamente a stomaco pieno, una volta al giorno prima di andare a letto");
        temp.setLink("https://www.my-personaltrainer.it/Foglietti-illustrativi/Folidex.html");
        temp.setPresa(false);
        list.add(temp);

        temp = new Medicina();
        temp.setNome("Cardioaspirina");
        temp.setDescrizione("La cardioaspirina è un farmaco a base di acido acetilsalicilico ed appartiene alla famiglia dei farmaci antitrombotici.");
        temp.setDosaggio("La dose raccomandata è: 1 compressa al giorno in un'unica somministrazione. Assuma il medicinale con un'abbondante quantità " +
                "di liquido (½ - 1 bicchiere d'acqua), prima dei pasti. ");
        temp.setFrequenza("Giorno");
        temp.setFrequenzaNum(1);
        temp.setOra("20:30");
        temp.setConsigliSupervisore("Una compressa al giorno in un'unica somministrazione, non oltre i 45 minuti dopo la fine del pasto");
        temp.setLink("https://www.my-personaltrainer.it/Foglietti-illustrativi/Cardioaspirin.html");
        temp.setPresa(false);
        list.add(temp);

        return list;

    }
}
