package pt.ipbeja.estig.po2.pandemic.model.Persons;

import java.util.Random;

public enum TypeImmune {

    IMMUNE, NOIMMUNE;

    public static TypeImmune getImmune() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }


}
