package pt.ipbeja.estig.po2.pandemic.model;

import pt.ipbeja.estig.po2.pandemic.model.Persons.Person;

import java.util.List;

/**
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public interface View {

    void populateWorld();

    void updatePosition(int index, int dx, int dy);

    void updateColor(Person person, int index);

    void updateGraph(int numInfects);

    void updateData(List<String> dataList);

}
