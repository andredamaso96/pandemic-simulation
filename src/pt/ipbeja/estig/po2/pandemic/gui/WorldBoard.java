package pt.ipbeja.estig.po2.pandemic.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pt.ipbeja.estig.po2.pandemic.model.*;
import pt.ipbeja.estig.po2.pandemic.model.Persons.Person;

import java.util.ArrayList;

/**
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class WorldBoard extends Pane {

    static public final Color[] STATE_COLORS = {Color.BLUE, Color.RED, Color.GREEN};
    private final int CELL_SIZE;
    private final int nLinesPane;
    private final int nColsPane;

    private Rectangle rectangle;
    public ArrayList<Rectangle> rectangles;

    private int x = 0;


    private World world;

    /**
     * Class constructor.
     *
     * @param world the model of game
     * @param size the size of square (cell)
     */
    public WorldBoard(World world, int size) {

        this.CELL_SIZE = size;
        this.nLinesPane = world.nLines() * CELL_SIZE;
        this.nColsPane = world.nCols() * CELL_SIZE;
        //this.setPrefSize(this.nLinesPane, this.nColsPane);
       // this.setPrefSize(this.nColsPane, this.nLinesPane);
        this.setMaxWidth(this.nColsPane);
        this.setPrefHeight(this.nLinesPane);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        //this.populateWorld();
        this.rectangle = null;
        this.rectangles = new ArrayList<>();


    }

    /**
     * Adds a rectangle to each person, and to the array of rectangles
     */
    public void populateWorld(){

        for(Person person : ContagiousBoard.world.persons){

            rectangles.add(addRectangle(person));

        }


    }

    /**
     * Update the square position
     *
     * @param dx where is set x
     * @param dy where is set y
     * @param index the index of the person, which is respective to the square
     */
    public void updatePosition(int dx, int dy, int index) {
        //TranslateTransition tt = new TranslateTransition(Duration.millis(200), this.rectangle);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), rectangles.get(index));
        tt.setByX(dx * CELL_SIZE);
        tt.setByY(dy * CELL_SIZE);
        tt.play();
    }

    /**
     * Update the square color
     *
     * @param person the person to update
     * @param index the index of the person, which is respective to the square
     */
    public void updateColor(Person person, int index){

        rectangles.get(index).setFill(color(person));

    }

    /**
     * Insert the color according to the type of person
     *
     * @param person the person to insert color
     * @return respective person's color
     */
    private Color color(Person person){

        if (person.getState() == 1) { //normal
            return Color.BLUE;
        } else if (person.getState() == 2) { //infected
            return Color.RED;
        } else if (person.getState() == 3) { //recovered
            return Color.GREEN;
        }

        return null;
    }

    /**
     * Insert the square according to the type of person
     *
     * @param person the person to insert square
     * @return respective person's square
     */
    private Rectangle addRectangle(Person person) {

        int line = person.cellPosition().getLine() * CELL_SIZE;
        int col = person.cellPosition().getCol() * CELL_SIZE;

        //Cell cell = world.map[position.getLine()][position.getCol()];

        Rectangle r = new Rectangle(col, line, CELL_SIZE, CELL_SIZE);

        r.setFill(color(person));

        Platform.runLater( () -> {
            this.getChildren().add(r);
        });
        return r;
    }


}
