package pt.ipbeja.estig.po2.pandemic.model.Persons;

import pt.ipbeja.estig.po2.pandemic.model.CellPosition;
import pt.ipbeja.estig.po2.pandemic.model.World;

/**
 * The imune person person for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class ImmunePerson extends Person {

    private TypeContagious typeContagious;
    private TypeImmune typeImmune;

    public ImmunePerson(CellPosition cellPosition, World model, int state) {
        super(cellPosition, model, state);
        this.typeContagious = TypeContagious.getContagious();
        this.typeImmune = TypeImmune.getImmune();
    }

    @Override
    public TypeContagious typeContagious() {
        return this.typeContagious;
    }

    @Override
    public TypeImmune typeImmune() {
        return this.typeImmune;
    }

    @Override
    public TypeSick typeSick() {
        return null;
    }

    public void setTypeContagious(TypeContagious typeContagious) {
        this.typeContagious = typeContagious;
    }

    public void setTypeImmune(TypeImmune typeImmune) {
        this.typeImmune = typeImmune;
    }
}
