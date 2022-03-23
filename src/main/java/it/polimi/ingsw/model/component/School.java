package it.polimi.ingsw.model.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {

    private Player owner;

    private final Board<Student> entrance;
    private Map<Color, Board<Student>> diningRoom;
    private final Board<Professor> professorsTable;
    private final Board<Tower> towersBoard;

    public School(Player owner, List<Student> students, List<Tower> towers) {
        this.owner = owner;

        this.entrance = new Board<Student>(students);

        this.diningRoom = new HashMap<>();
        this.diningRoom.put(Color.RED, new Board<Student>());
        this.diningRoom.put(Color.YELLOW, new Board<Student>());
        this.diningRoom.put(Color.BLUE, new Board<Student>());
        this.diningRoom.put(Color.GREEN, new Board<Student>());
        this.diningRoom.put(Color.PINK, new Board<Student>());

        this.professorsTable = new Board<Professor>();
        this.towersBoard = new Board<Tower> (towers);
    }

    public Player getOwner(){
        return this.owner;
    }

    public Board<Student> getEntrance() {
        return this.entrance;
    }

    public Board<Student> getDiningRoomByColor(Color color) {
        return this.diningRoom.get(color);
    }

    public Board<Professor> getProfessorsTable() {
        return this.professorsTable;
    }

    public Board<Tower> getTowersBoard() {
        return this.towersBoard;
    }

    public void moveStudentDiningRoom(Student student) {
        this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
    }

    public void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }
}
