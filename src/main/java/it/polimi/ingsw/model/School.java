package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.List;

public class School {
    private Player owner;

    private List<Student> diningRoom;
    private List<Student> entrance;
    private List<Professor> profTable;
    private List<Tower> towers;

    public School(Player owner, List<Student> students) {

        this.owner = owner;
        entrance = new ArrayList<Student>(students);
        profTable = new ArrayList<Professor>();
        diningRoom = new ArrayList<Student>();


    }


    public Player getOwner() {
        return owner;
    }


}
