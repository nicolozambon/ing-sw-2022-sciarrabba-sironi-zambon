package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ModelSerializable implements Serializable {
    private static final long serialVersionUID = 987654321L;
    private Model model;
    private Map<Integer, SchoolSerializable> schools;
    private List<IslandSerialiazable> islands;


    public ModelSerializable (Model model) {
        this.model = model;
        this.schools = new HashMap<>();
        this.islands = new ArrayList<>();
    }

}

class SchoolSerializable implements Serializable { //A SchoolSerializable for all players.
    private static final long serialVersionUID = 987654321L;
    Map<Color, Integer> diningRoom = new HashMap<>();
    ArrayList<Color> entrance = new ArrayList<>(); //Ordered by #Position of Entrance in the board.
    ArrayList<Color> profTable = new ArrayList<>();
    int numTowers;
    TowerColor towerColor;

    SchoolSerializable(School school, TowerColor towerColor) {
        for (Color color : Color.values()) {
            diningRoom.put(color, school.getDiningRoomByColor(color).getNumPawns());
        }
        for (Student student : school.getEntrance().getPawns()) {
            entrance.add(student.getColor());
        }
        numTowers = school.getTowersBoard().getNumPawns();
        this.towerColor = towerColor;
        for (Professor professor : school.getProfessorsTable().getPawns()) {
            profTable.add(professor.getColor());
        }
    }
}


class IslandSerialiazable implements Serializable {
    private static final long serialVersionUID = 987654321L;

    private Map<Color, Integer> students;
    private boolean isThereTower;

    IslandSerialiazable(Island island) {
        this.students = new HashMap<>();
        this.isThereTower = false;

        for (Color color : Color.values()) {
            students.put(color, island.countStudentsByColor(color));
        }

        if (island.getTower() != null) {
            isThereTower = true;
        }

    }
}