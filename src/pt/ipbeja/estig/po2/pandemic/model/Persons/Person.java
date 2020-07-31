package pt.ipbeja.estig.po2.pandemic.model.Persons;

import pt.ipbeja.estig.po2.pandemic.gui.ContagiousBoard;
import pt.ipbeja.estig.po2.pandemic.gui.WorldBoard;

import java.util.Objects;
import javafx.scene.shape.Rectangle;
import pt.ipbeja.estig.po2.pandemic.model.Cell;
import pt.ipbeja.estig.po2.pandemic.model.CellPosition;
import pt.ipbeja.estig.po2.pandemic.model.World;


/**
 * The person for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public abstract class Person extends Cell {

    private int dx;
    private int dy;
    private WorldBoard pane;
    private int state;

    private int numInfects = 1;

    //public static int healtime = 5 * 50;
    public static int healtime = World.rand.nextInt(5 * 50);
    private int sickTime = 0;


    /**
     * Class constructor
     * @param cellPosition the position
     * @param model the model of game
     * @param state the state of person
     */
    public Person(CellPosition cellPosition, World model, int state) {
        super(cellPosition, model);
        this.state = state;
    }


    /**
     * Checks if a target position is within the board's limits.
     * @return true, the moviment
     */
    public boolean randomMove() {

        //if(Math.random()<.1)
        if(Math.random()<.1) {
            final int[] v = {-1, 0, 1};
            this.dx = v[World.rand.nextInt(3)];
            this.dy = v[World.rand.nextInt(3)];
            if (dx == 0 && dy == 0) {// to force a move
                dx = 1;
            }
        }

        int line = cellPosition.getLine();
        int col = cellPosition.getCol();

        if (line + dy < 0 || line + dy > ContagiousBoard.world.nLines() - 1) {
            dy = -dy;
        }
        if (col + dx < 0 || col + dx > ContagiousBoard.world.nCols() - 1) {
            dx = -dx;
        }


        this.cellPosition = new CellPosition(
                this.cellPosition.getLine() + dy,
                this.cellPosition.getCol() + dx);


        return true;


    }


    /**
     * Checks if a person sick is cured
     *
     */
    public void cureSick(){

        if(this.getState() == 2){

            sickTime++;
            if (sickTime > healtime){
                this.setState(3);
                super.getModel().changePerson(this);

            }
        }

    }

    public int dx() {
        return this.dx;
    }

    public int dy() {
        return this.dy;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNumInfects() {
        return numInfects;
    }

    public abstract TypeContagious typeContagious();
    public abstract TypeImmune typeImmune();
    public abstract TypeSick typeSick();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return dx == person.dx &&
                dy == person.dy &&
                state == person.state &&
                numInfects == person.numInfects &&
                Objects.equals(pane, person.pane);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dx, dy, pane, state, numInfects);
    }


}
