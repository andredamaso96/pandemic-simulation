package pt.ipbeja.estig.po2.pandemic.model;

import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.pandemic.model.Persons.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

class WorldTest {

    // Req. E3 - Código de teste movimento

    @Test
    void movePerson(){

        World world = new World(new ViewTest(), 10, 10);


        Person healhyPerson =  new HealhyPerson(new CellPosition(5,5), world, 1);

        boolean validMove = world.movePerson(healhyPerson,5,4);
        //boolean validMove = healhyPerson.movePerson(5,4);

        assertTrue(validMove);
        assertEquals(5, healhyPerson.cellPosition.getLine());
        assertEquals(4, healhyPerson.cellPosition.getCol());

        boolean validMove2 = world.movePerson(healhyPerson,6,4);

        assertTrue(validMove2);
        assertEquals(6, healhyPerson.cellPosition.getLine());
        assertEquals(4, healhyPerson.cellPosition.getCol());

        boolean validMove3 = world.movePerson(healhyPerson,6,5);

        assertTrue(validMove3);
        assertEquals(6, healhyPerson.cellPosition.getLine());
        assertEquals(5, healhyPerson.cellPosition.getCol());



    }

    @Test
    void movePersonToEmptyCell(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(5,5), world, 1);

        boolean validMove = world.movePerson(healhyPerson,5,4);

        assertTrue(validMove);
        assertEquals(5, healhyPerson.cellPosition.getLine());
        assertEquals(4, healhyPerson.cellPosition.getCol());


    }

    @Test
    void movePersonToInvalidPositionUp(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(0,5), world, 1);

        boolean invalidMove = world.movePerson(healhyPerson, -1, 5);

        assertFalse(invalidMove);
        assertEquals(0, healhyPerson.cellPosition.getLine());
        assertEquals(5, healhyPerson.cellPosition.getCol());

    }

    @Test
    void movePersonToInvalidPositionLeft(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(3,0), world, 1);

        boolean invalidMove = world.movePerson(healhyPerson, 3, -1);

        assertFalse(invalidMove);
        assertEquals(3, healhyPerson.cellPosition.getLine());
        assertEquals(0, healhyPerson.cellPosition.getCol());

    }



    @Test
    void movePersonToInvalidPositionDown(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(9,0), world, 1);

        boolean invalidMove = world.movePerson(healhyPerson, 10, 0);

        assertFalse(invalidMove);
        assertEquals(9, healhyPerson.cellPosition.getLine());
        assertEquals(0, healhyPerson.cellPosition.getCol());
    }

    @Test
    void movePersonToInvalidPositionRight(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(0,9), world, 1);

        boolean invalidMove = world.movePerson(healhyPerson, 0, 10);

        assertFalse(invalidMove);
        assertEquals(0, healhyPerson.cellPosition.getLine());
        assertEquals(9, healhyPerson.cellPosition.getCol());


    }


    // Req. E4 - Código de teste contágio

    @Test
    void CollisionSickToHealhy(){

        World world = new World(new ViewTest(), 10, 10);

        Person sickPerson = new SickPerson(new CellPosition(4, 5), world, 2);
        Person healhyPerson =  new HealhyPerson(new CellPosition(6,5), world, 1);

        world.movePerson(sickPerson,5,5);
        world.movePerson(healhyPerson, 5,5);


        world.testCollision(sickPerson, healhyPerson);

        assertEquals(sickPerson.getState(), healhyPerson.getState());


    }

    @Test
    void CollisionSickToHealhy2(){

        World world = new World(new ViewTest(), 10, 10);

        Person sickPerson = new SickPerson(new CellPosition(4, 5), world, 2);
        Person healhyPerson =  new HealhyPerson(new CellPosition(6,5), world, 1);

        world.movePerson(sickPerson,5,5);


        world.testCollision(sickPerson, healhyPerson);

        assertEquals(sickPerson.getState(), healhyPerson.getState());


    }


    @Test
    void CollisionHealhyToSick(){

        World world = new World(new ViewTest(), 10, 10);

        Person healhyPerson =  new HealhyPerson(new CellPosition(4,5), world, 1);
        Person sickPerson = new SickPerson(new CellPosition(6, 5), world, 2);


        world.movePerson(healhyPerson, 5,5);
        world.movePerson(sickPerson,5,5);


        world.testCollision(healhyPerson, sickPerson);
        //healhyPerson.collision(sickPerson);


        assertEquals(sickPerson.getState(), healhyPerson.getState());


    }

    @Test
    void NoCollision(){

        World world = new World(new ViewTest(), 10, 10);

        Person sickPerson = new SickPerson(new CellPosition(4, 5), world, 2);
        Person healhyPerson =  new HealhyPerson(new CellPosition(6,5), world, 1);


        //world.movePerson(sickPerson,5,5);
        //world.movePerson(healhyPerson, 5,5);

        world.testCollision(sickPerson, healhyPerson);

        assertEquals(healhyPerson.getState(), 1);


    }

    @Test
    void CollisionSickToSick(){

        World world = new World(new ViewTest(), 10, 10);

        Person sickPerson = new SickPerson(new CellPosition(4, 5), world, 2);
        Person sickPerson2 = new SickPerson(new CellPosition(6, 5), world, 2);


        world.movePerson(sickPerson,5,5);
        world.movePerson(sickPerson2, 5,5);

        world.testCollision(sickPerson, sickPerson2);

        assertEquals(sickPerson.getState(), sickPerson2.getState());
        assertEquals(sickPerson.getState(), 2);
        assertEquals(sickPerson2.getState(), 2);



    }

    @Test
    void CollisionSickToImune(){

        World world = new World(new ViewTest(), 10, 10);

        Person sickPerson = new SickPerson(new CellPosition(4, 5), world, 2);
        Person imunePerson = new ImmunePerson(new CellPosition(6, 5), world, 3);


        world.movePerson(sickPerson,5,5);
        world.movePerson(imunePerson, 5,5);

        world.testCollision(sickPerson, imunePerson);

        //assertEquals(sickPerson.getState(), imunePerson.getState());
        assertEquals(sickPerson.getState(), 2);
        assertEquals(imunePerson.getState(), 3);

    }



}