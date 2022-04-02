package it.polimi.ingsw.model.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.enums.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {

    private Player owner;

    private final Board<Student> entrance;
    private final Map<Color, Board<Student>> diningRoom;
    private final Board<Professor> professorsTable;
    private final Board<Tower> towersBoard;

    public School(Player owner, List<Student> students, List<Tower> towers) {
        this.owner = owner;

        this.entrance = new Board<Student>(students);

        this.diningRoom = new HashMap<>();
        this.diningRoom.put(Color.RED, new Board<>());
        this.diningRoom.put(Color.YELLOW, new Board<>());
        this.diningRoom.put(Color.BLUE, new Board<>());
        this.diningRoom.put(Color.GREEN, new Board<>());
        this.diningRoom.put(Color.PINK, new Board<>());

        this.professorsTable = new Board<>();
        this.towersBoard = new Board<> (towers);
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

    public boolean moveStudentDiningRoom(Student student) {
        if (this.diningRoom.get(student.getColor()).getPawns().size() < 10) {
            this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
        }

        if ((this.diningRoom.get(student.getColor()).getPawns().indexOf(student)) % 3 == 2) return true;
        return false;
    }

    public void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }

    public void takeStudentsFromCloud(Cloud cloud){
        for (Student student : cloud.getPawns()){
            entrance.moveInPawn(student, cloud);
        }
    }

    public void setProfessor(Professor professor, Board<Professor> src) {
        professorsTable.moveInPawn(professor, src);
    }

}
