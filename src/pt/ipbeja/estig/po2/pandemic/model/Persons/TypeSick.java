package pt.ipbeja.estig.po2.pandemic.model.Persons;

import java.util.Random;

public enum TypeSick {

    SYMPTOM, NOSYMPTOM;

    public static TypeSick getSymptom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

}
