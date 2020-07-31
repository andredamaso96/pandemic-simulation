package pt.ipbeja.estig.po2.pandemic.model.Persons;

import pt.ipbeja.estig.po2.pandemic.model.CellPosition;
import pt.ipbeja.estig.po2.pandemic.model.Persons.Person;
import pt.ipbeja.estig.po2.pandemic.model.World;

/**
 * The sick person for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class SickPerson extends Person {

    private TypeSick typeSick;
    private TypeContagious typeContagious;

    public SickPerson(CellPosition cellPosition, World model, int state) {
        super(cellPosition, model, state);
        this.typeContagious = TypeContagious.getContagious();
        this.typeSick = TypeSick.getSymptom();
    }

    @Override
    public TypeContagious typeContagious() {
        return typeContagious;
    }

    @Override
    public TypeImmune typeImmune() {
        return null;
    }

    @Override
    public TypeSick typeSick() {
        return typeSick;
    }


}
