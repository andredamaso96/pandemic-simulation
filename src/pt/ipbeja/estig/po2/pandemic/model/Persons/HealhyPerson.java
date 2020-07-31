package pt.ipbeja.estig.po2.pandemic.model.Persons;

import pt.ipbeja.estig.po2.pandemic.model.CellPosition;
import pt.ipbeja.estig.po2.pandemic.model.Persons.Person;
import pt.ipbeja.estig.po2.pandemic.model.World;

/**
 * The healhy person for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class HealhyPerson extends Person {


    public HealhyPerson(CellPosition cellPosition, World model, int state) {
        super(cellPosition, model, state);
    }

    @Override
    public TypeContagious typeContagious() {
        return null;
    }

    @Override
    public TypeImmune typeImmune() {
        return null;
    }

    @Override
    public TypeSick typeSick() {
        return null;
    }

}
