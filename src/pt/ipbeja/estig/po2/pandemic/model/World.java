package pt.ipbeja.estig.po2.pandemic.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import javafx.scene.shape.Rectangle;
import pt.ipbeja.estig.po2.pandemic.model.Persons.*;

/**
 * The model for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class World {
    public static final Random rand = new Random();

    private View view;
    private final int nLines;
    private final int nCols;
    private int peopleSize;
    private int healhySize;
    private int sickSize;
    private int immuneSize;

    public Cell[][] map;
    public ArrayList<Person> persons;

    private boolean isFile = false;
    private List<String> healhyPeople;
    private List<String> immunePeople;
    private List<String> sickPeople;
    private List<String> listData;
    private List<String> healhyData;
    private List<String> sickData;
    private List<String> imuneData;
    Thread t1;


    private int counterSick = 0;

    private final Object pauseLock = new Object();


    /**
     * Class constructor.
     *
     * @param view system interface
     * @param nLines the number of lines
     * @param nCols the number of cols
     */
    public World(View view, int nLines, int nCols) {
        this.view = view;
        this.nLines = nLines;
        this.nCols = nCols;
        this.map = new Cell[nLines][nCols];
        this.persons = new ArrayList<>();
        this.listData = new ArrayList<>();
        this.healhyData = new ArrayList<>();
        this.sickData = new ArrayList<>();
        this.imuneData = new ArrayList<>();
        /*this.healhyPeople = new ArrayList<>();
        this.immunePeople = new ArrayList<>();
        this.sickPeople = new ArrayList<>();*/

    }



    /**
     * Returns the number of people
     *
     * @param healhySize the number of healthy people
     * @param sickSize the number of sick people
     * @param immuneSize the number of immune people
     */
    public void setSize(int healhySize, int sickSize, int immuneSize){

        //this.peopleSize = quantity;
        this.healhySize = healhySize;
        this.sickSize = sickSize;
        this.immuneSize = immuneSize;

    }

    public void setPersons(boolean b, List<String> listHealhy, List<String> listImune, List<String> listSick){

        isFile = b;
        healhyPeople = listHealhy;
        immunePeople = listImune;
        sickPeople = listSick;

    }

    /**
     * Starts the simulation
     *
     */
    public void start() {

       t1 = new Thread( () -> {
           this.populate();
           this.simulate(500);
        });

       t1.start();

    }

    public void stop() {

        t1.suspend();

    }

    public void restart(){

        try {
            Thread.sleep(200);
            t1.resume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //t1.resume();

    }

    public int nLines() {
        return this.nLines;
    }

    public int nCols() {
        return this.nCols;
    }


    /**
     * Start people's initial positions
     *
     */
    public void populate() {

        // Se o jogo estiver a correr pelo ficheiro de texto ...
        if(isFile){

            for(String hp : healhyPeople){

                String[] lc = hp.split(" ");
                int line = Integer.parseInt(lc[0]);
                int col = Integer.parseInt(lc[1]);
                Person healhyPerson = new HealhyPerson(new CellPosition(line, col), this, 1);
                persons.add(healhyPerson);
                map[line][col] = healhyPerson;

            }

            for(String sp : sickPeople){

                String[] lc = sp.split(" ");
                int line = Integer.parseInt(lc[0]);
                int col = Integer.parseInt(lc[1]);
                Person sickPerson = new SickPerson(new CellPosition(line, col), this, 2);
                persons.add(sickPerson);
                map[line][col] = sickPerson;

            }

            for(String ip : immunePeople){

                String[] lc = ip.split(" ");
                int line = Integer.parseInt(lc[0]);
                int col = Integer.parseInt(lc[1]);
                Person imunePerson = new ImmunePerson(new CellPosition(line, col), this, 3);
                persons.add(imunePerson);
                map[line][col] = imunePerson;

            }


        }else {

            // modo jogo normal
            /*Person sick = new SickPerson(new CellPosition(2,2), this, 2);
            persons.add(sick);
            map[2][2] = sick;*/

            for (int i = 0; i < healhySize; i++) {

                int line = (int) (Math.random() * this.nLines);
                int col = (int) (Math.random() * this.nCols);

                persons.add(new HealhyPerson(new CellPosition(line,
                        col), this, 1));
                map[line][col] = persons.get(i);

            }

            for (int i = 0; i < sickSize; i++) {

                int line = (int) (Math.random() * this.nLines);
                int col = (int) (Math.random() * this.nCols);

                persons.add(new SickPerson(new CellPosition(line,
                        col), this, 2));
                map[line][col] = persons.get(i);

            }

            for (int i = 0; i < immuneSize; i++) {

                int line = (int) (Math.random() * this.nLines);
                int col = (int) (Math.random() * this.nCols);

                persons.add(new ImmunePerson(new CellPosition(line,
                        col), this, 3));
                map[line][col] = persons.get(i);

            }


        }


        this.view.populateWorld();

    }

    /**
     * Performs the simulation of movements, with contagion and cure
     *
     * @param nIter the number of simulation
     */
    private void simulate(int nIter) {

        for (int i = 0; i < nIter; i++) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.checkEmptyCell();
            this.move();
            this.resolveSick();
            this.resolveCollisions();
            this.updateCountSick();
            this.getData();

        }

    }

    /**
     * Get the data for recording
     *
     */
    private void getData() {

        listData.removeAll(listData);
        healhyData.removeAll(healhyData);
        sickData.removeAll(sickData);
        imuneData.removeAll(imuneData);

        listData.add(String.valueOf(this.nLines));
        listData.add(String.valueOf(this.nCols));

        for(Person person : persons){

            if(person.getState() == 1){
                int line = person.cellPosition().getLine();
                int col = person.cellPosition().getCol();
                String position = line + " " + col;
                healhyData.add(position);
            }

            if(person.getState() == 2){
                int line = person.cellPosition().getLine();
                int col = person.cellPosition().getCol();
                String position = line + " " + col;
                sickData.add(position);
            }

            if(person.getState() == 3){
                int line = person.cellPosition().getLine();
                int col = person.cellPosition().getCol();
                String position = line + " " + col;
                imuneData.add(position);
            }

        }

        listData.add("healthy");
        listData.addAll(healhyData);

        listData.add("sick");
        listData.addAll(sickData);

        listData.add("immune");
        listData.addAll(imuneData);

        //view.updateData(listData);

    }

    public List<String> setData(){

        return listData;

    }

    private void checkEmptyCell() {

        for (int line = 0; line < nLines; line++) {
            for (int col = 0; col < nCols; col++) {

                if(map[line][col] == null){
                    map[line][col] = new EmptyCell(new CellPosition(line,col), this);
                }
            }
        }

    }

    private void move() {

        for(Person person : persons){
            if (person.randomMove()){
                this.view.updatePosition(persons.indexOf(person), person.dx(), person.dy());
            }
        }

    }

    /**
     * Moves the person if the move is valid.
     *
     * @param person the person to move to
     * @param line the line to move to
     * @param col the column to move to
     * @return true if the person moved, false otherwise
     */
    public boolean movePerson(Person person, int line, int col){

        CellPosition position = new CellPosition(line, col);

        if(position.isInside(nLines, nCols)){

            person.cellPosition = new CellPosition(line, col);
            return true;
        }else return false;

    }

    /**
     * Resolve contagious
     *
     */
    private void resolveCollisions(){

       for(int i =0; i < persons.size();i++) {
            for(int j = i+1 ; j < persons.size();j++){
                //for each unique pair invoke the collision detection code
                //persons.get(i).collision(persons.get(j));
                testCollision(persons.get(i), persons.get(j));
            }
        }

    }


    /**
     * Resolve cure
     *
     */
    private void resolveSick(){

        for(Person person : persons){
            person.cureSick();
        }
    }

    /**
     * Checks if there is a collision between the current person and the other
     *
     * @param person
     * @param otherPerson
     */
    public void testCollision(Person person, Person otherPerson){

        Rectangle r1 = new Rectangle(otherPerson.cellPosition().getLine() * 10, otherPerson.cellPosition().getCol() * 10, 10,10);
        Rectangle r2 = new Rectangle(person.cellPosition().getLine() *10, person.cellPosition().getCol() * 10, 10,10);


        if(r1.intersects(r2.getLayoutBounds())){

            if(person.getState() == 2 && otherPerson.getState() == 1) { //case person 1 is infected and person 1 is not
                otherPerson.setState(2);
                changePerson(otherPerson);
                System.out.println("tocaram");
            }else if(person.getState() == 1 && otherPerson.getState() == 2) { //case person 2 is infected and person 1 is
                person.setState(2);
                changePerson(person);
                System.out.println("tocaram");
            }

        }

    }


    /**
     * Change the person color
     *
     * @param person
     */
    public void changePerson(Person person) {

        this.view.updateColor(person, persons.indexOf(person));

    }


    /**
     * Count sicks persons
     */
    public void updateCountSick(){

        int count = 0;


        for(Person person : persons){
            if(person.getState() == 2) {
            //if(person instanceof SickPerson) {
                count++;
                counterSick++;
            }
        }

        this.view.updateGraph(counterSick);
        counterSick = 0;

    }

}