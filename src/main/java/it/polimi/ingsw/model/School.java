package it.polimi.ingsw.model;

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

    protected School(Player owner, List<Student> students, List<Tower> towers) {
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

    protected Player getOwner(){
        return this.owner;
    }

    protected Board<Student> getEntrance() {
        return this.entrance;
    }

    protected Board<Student> getDiningRoomByColor(Color color) {
        return this.diningRoom.get(color);
    }

    protected Board<Professor> getProfessorsTable() {
        return this.professorsTable;
    }

    protected Board<Tower> getTowersBoard() {
        return this.towersBoard;
    }

    protected void moveStudentDiningRoom(Student student) {
        if (this.diningRoom.get(student.getColor()).getNumPawns() < 10) {
            this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
        }
    }

    protected void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }

    protected void takeStudentsFromCloud(Cloud cloud){
        for (Student student : cloud.getPawns()){
            entrance.moveInPawn(student, cloud);
        }
    }

    protected void setProfessor(Professor professor, Board<Professor> src) {
        professorsTable.moveInPawn(professor, src);
    }

}
