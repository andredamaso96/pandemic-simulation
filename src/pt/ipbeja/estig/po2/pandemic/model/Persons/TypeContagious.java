package pt.ipbeja.estig.po2.pandemic.model.Persons;

import java.util.Random;

public enum TypeContagious {

    CONTAGIOUS, NO_CONTAGIOUS;

    public static TypeContagious getContagious() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

}
