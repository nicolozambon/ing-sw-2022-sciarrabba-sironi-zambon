package it.polimi.ingsw.model;

import java.util.List;

public class School {

    private Player owner;

    public final Board<Student> RedDiningTable;
    public final Board<Student> GreenDiningTable;
    public final Board<Student> YellowDiningTable;
    public final Board<Student> BlueDiningTable;
    public final Board<Student> PinkDiningTable;

    public final Board<Student> entrance;
    public final Board<Professor> professorsTable;
    public final Board<Tower> towersBoard;

    public School(Player owner, List<Student> students, List<Tower> towers) {
        this.owner = owner;

        RedDiningTable = new Board<Student>();
        GreenDiningTable = new Board<Student>();
        YellowDiningTable = new Board<Student>();
        BlueDiningTable = new Board<Student>();
        PinkDiningTable = new Board<Student>();

        entrance = new Board<Student>(students);
        professorsTable = new Board<Professor>();
        this.towersBoard = new Board<Tower> (towers);
    }

    public Player getOwner(){
        return owner;
    }

}
