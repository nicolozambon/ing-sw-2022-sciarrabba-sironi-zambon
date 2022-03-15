package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class School {

    private Player owner;


    private List<Student> diningRoom;
    private List<Student> entrance;
    private List<Professor> professors;
    private List<Tower> towers;

    public School(Player owner, List<Student> students, List<Tower> towers) {

        this.owner = owner;
        this.entrance = new ArrayList<Student>(students);
        this.professors = new ArrayList<Professor>();
        this.diningRoom = new ArrayList<Student>();
        this.towers = new ArrayList<Tower>(towers);

    }

    public Player getOwner() {
        return owner;
    }

    public int getNumOfTower() {
        return towers.size();
    }

    public List<Student> getDiningRoomByColor(Color color) {

        List<Student> list;
        list = diningRoom
                .stream()
                .filter(x -> x.getColor() == color)
                .toList();

        return list;
    }

    public List<Student> getDiningRoom() {
        return new ArrayList<Student>(diningRoom);
    }

    public List<Student> getEntrance() {
        return new ArrayList<Student>(entrance);
    }

    public List<Professor> getProfTable() {
        return new ArrayList<Professor>(professors);
    }

    public List<Tower> getTowers() {
        return new ArrayList<Tower>(towers);
    }
}
