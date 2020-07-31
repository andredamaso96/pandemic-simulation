package pt.ipbeja.estig.po2.pandemic.model;

import pt.ipbeja.estig.po2.pandemic.gui.ContagiousBoard;
import pt.ipbeja.estig.po2.pandemic.gui.WorldBoard;

import java.util.Objects;

/**
 * The cell for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public abstract class Cell {

    protected CellPosition cellPosition;
    private World model;


    public Cell(CellPosition cellPosition, World model) {
        this.cellPosition = cellPosition;
        this.model = model;
    }

    public CellPosition cellPosition() {
        return cellPosition;
    }

    public World getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(cellPosition, cell.cellPosition) &&
                Objects.equals(model, cell.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellPosition, model);
    }
}
